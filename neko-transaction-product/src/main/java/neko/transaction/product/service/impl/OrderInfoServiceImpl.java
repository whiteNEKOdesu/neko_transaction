package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.*;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.commonbase.utils.exception.OrderOverTimeException;
import neko.transaction.commonbase.utils.exception.StockNotEnoughException;
import neko.transaction.product.config.AliPayTemplate;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.ware.WareInfoFeignService;
import neko.transaction.product.mapper.OrderInfoMapper;
import neko.transaction.product.service.OrderInfoService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.to.AliPayTo;
import neko.transaction.product.to.LockStockTo;
import neko.transaction.product.to.NewOrderRedisTo;
import neko.transaction.product.to.RabbitMQMessageTo;
import neko.transaction.product.vo.NewOrderInfoVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private AliPayTemplate aliPayTemplate;

    @Resource(name = "threadPoolExecutor")
    private Executor threadPool;

    /**
     * 生成 token 保证创建订单接口幂等性，用于创建订单传入
     * @return 创建订单传入的 token
     */
    @Override
    public String getPreOrderToken() {
        //生成 token 保证创建订单接口幂等性，用于创建订单传入
        String token = IdUtil.randomUUID();
        String key = Constant.PRODUCT_REDIS_PREFIX + "order_id:" + StpUtil.getLoginId().toString() + ":" + token;

        stringRedisTemplate.opsForValue().setIfAbsent(key,
                token,
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 添加订单
     * @param vo 提交订单vo
     * @return 订单号
     */
    @Override
    public String newOrder(NewOrderInfoVo vo) throws ExecutionException, InterruptedException {
        String token = vo.getToken();
        String uid = StpUtil.getLoginId().toString();
        //创建订单幂等性保证的 token 的 redis key
        String key = Constant.PRODUCT_REDIS_PREFIX + "order_id:" + uid + ":" + token;

        //step1 -> 验证订单创建幂等性保证的 token 是否合法，0 -> token 失效，1 -> 删除成功，合法
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //原子验证令牌和删除令牌
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                token);

        if(result == null || result.equals(0L)){
            throw new NoSuchResultException("无此预生成订单信息");
        }

        //订单号
        String orderId = IdWorker.getTimeId();

        //step2 -> 向延迟队列发送订单号，用于超时解锁库存
        RabbitMQMessageTo<String> rabbitMQMessageTo = RabbitMQMessageTo.generateMessage(orderId, MQMessageType.ORDER_STATUS_CHECK_TYPE);
        //在CorrelationData中设置回退消息
        CorrelationData correlationData = new CorrelationData(MQMessageType.ORDER_STATUS_CHECK_TYPE.toString());
        String jsonMessage = JSONUtil.toJsonStr(rabbitMQMessageTo);
        String notAvailable = "not available";
        correlationData.setReturned(new ReturnedMessage(new Message(jsonMessage.getBytes(StandardCharsets.UTF_8)),
                0,
                notAvailable,
                notAvailable,
                notAvailable));
        //向延迟队列发送订单号，用于超时解锁库存
        rabbitTemplate.convertAndSend(RabbitMqConstant.ORDER_EXCHANGE_NAME,
                RabbitMqConstant.ORDER_DEAD_LETTER_ROUTING_KEY_NAME,
                jsonMessage,
                correlationData);

        //原子类标记是否在多线程中出现异常
        AtomicBoolean isException = new AtomicBoolean(false);

        //step3.1 -> 异步获取订单中的商品信息
        CompletableFuture<Map<String,ProductInfo>> getProductInfoTask = CompletableFuture.supplyAsync(() -> {
            //stream流收集商品id
            List<String> productIds = vo.getNewOrderProductInfos().stream().map(NewOrderInfoVo.NewOrderProductInfo::getProductId)
                    .collect(Collectors.toList());
            //获取商品id所涉及的商品信息
            List<ProductInfo> productInfos = productInfoService.listByIds(productIds);
            //map 映射关系: productId -> productInfo
            Map<String,ProductInfo> productInfoMap = new HashMap<>();
            for(ProductInfo productInfo : productInfos){
                productInfoMap.put(productInfo.getProductId(), productInfo);
            }

            return productInfoMap;
        }, threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        //step3.1.1 -> 异步计算订单总价
        CompletableFuture<BigDecimal> calculateCostTask = getProductInfoTask.thenApplyAsync((productInfoMap -> {
            if(productInfoMap == null || productInfoMap.isEmpty()){
                isException.set(true);

                return null;
            }

            //订单总价格
            BigDecimal cost = new BigDecimal("0")
                    .setScale(2, BigDecimal.ROUND_DOWN);

            for (NewOrderInfoVo.NewOrderProductInfo newOrderProductInfo : vo.getNewOrderProductInfos()) {
                //通过 hashMap 获取商品信息
                ProductInfo productInfo = productInfoMap.get(newOrderProductInfo.getProductId());
                cost = cost.add(productInfo.getPrice().multiply(new BigDecimal(newOrderProductInfo.getNumber().toString())));
            }

            return cost.setScale(2, BigDecimal.ROUND_DOWN);
        }), threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        //step3.1.1.1 -> 异步生成支付宝支付页面
        CompletableFuture<Void> alipayTask = calculateCostTask.thenAcceptAsync(cost -> {
            if (cost == null) {
                return;
            }

            AliPayTo aliPayTo = new AliPayTo();
            aliPayTo.setOut_trade_no(orderId)
                    .setSubject("NEKO_TRANSACTION")
                    //设置折扣价格
                    .setTotal_amount(cost.toString())
                    .setBody("NEKO_TRANSACTION");
            String alipayPageKey = Constant.PRODUCT_REDIS_PREFIX + "order:" + uid + orderId + ":pay_page";
            //将支付宝支付页面信息存入 redis
            try {
                stringRedisTemplate.opsForValue().setIfAbsent(alipayPageKey,
                        aliPayTemplate.pay(aliPayTo),
                        1000 * 60 * 4,
                        TimeUnit.MILLISECONDS);
            }catch (AlipayApiException e){
                e.printStackTrace();
            }
        }, threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        //step3.1.1.2 -> 异步记录订单信息到订单表
        CompletableFuture<Void> orderLogTask = calculateCostTask.thenAcceptAsync(cost -> {
            if (cost == null) {
                return;
            }

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderId)
                    .setUid(uid)
                    .setCost(cost)
                    .setActualCost(cost);

            //记录订单信息到订单表
            this.baseMapper.insert(orderInfo);
        }, threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        //step3.1.2 -> 将商品信息记录到 redis 中
        CompletableFuture<Void> redisLogTask = getProductInfoTask.thenAcceptAsync((productInfoMap -> {
            if (productInfoMap == null || productInfoMap.isEmpty()) {
                isException.set(true);

                return;
            }

            NewOrderRedisTo newOrderRedisTo = new NewOrderRedisTo();
            //设置订单号
            newOrderRedisTo.setOrderId(orderId);
            List<NewOrderRedisTo.ProductsInOrder> productsInOrders = new ArrayList<>();
            for (NewOrderInfoVo.NewOrderProductInfo newOrderProductInfo : vo.getNewOrderProductInfos()) {
                //要购买的商品id
                String productId = newOrderProductInfo.getProductId();
                //购买数量
                Integer number = newOrderProductInfo.getNumber();
                //通过 hashMap 获取商品信息
                ProductInfo productInfo = productInfoMap.get(productId);

                //添加订单的 redis to 的内部类，存放商品信息
                NewOrderRedisTo.ProductsInOrder productsInOrder = new NewOrderRedisTo.ProductsInOrder();
                productsInOrder.setProductId(productId)
                        .setCost(productInfo.getPrice())
                        .setActualCost(productInfo.getPrice())
                        .setNumber(number);

                //添加到 redis to 中 订单中所有的商品信息的集合
                productsInOrders.add(productsInOrder);
            }
            newOrderRedisTo.setProductsInOrders(productsInOrders);

            String productInfoKey = Constant.PRODUCT_REDIS_PREFIX + "order:" + uid + orderId + ":product_info";
            //记录订单商品信息到 redis 中
            stringRedisTemplate.opsForValue().setIfAbsent(productInfoKey,
                    JSONUtil.toJsonStr(newOrderRedisTo),
                    4,
                    TimeUnit.HOURS);
        }), threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        //step3.2 -> 锁定库存
        CompletableFuture<Void> lockStockTask = CompletableFuture.runAsync(() -> {
            LockStockTo lockStockTo = new LockStockTo();
            //收集锁定库存的商品信息
            List<LockStockTo.LockProductInfo> lockProductInfos = vo.getNewOrderProductInfos().stream()
                    .map(newOrderProductInfo -> new LockStockTo.LockProductInfo()
                            .setProductId(newOrderProductInfo.getProductId())
                            .setLockNumber(newOrderProductInfo.getNumber())).collect(Collectors.toList());
            lockStockTo.setOrderId(orderId)
                    .setLockProductInfos(lockProductInfos);

            //远程调用库存微服务锁定库存
            ResultObject<Object> r = wareInfoFeignService.lockStock(lockStockTo);
            if(!r.getResponseCode().equals(200)){
                isException.set(true);
            }
        }, threadPool).exceptionally(e -> {
            isException.set(true);
            e.printStackTrace();

            return null;
        });

        CompletableFuture.allOf(alipayTask, orderLogTask, redisLogTask, lockStockTask).get();

        if(isException.get()){
            throw new StockNotEnoughException("库存不足");
        }

        return orderId;
    }

    /**
     * 根据订单号获取支付宝支付页面
     * @param orderId 订单号
     * @param token 用户登录认证的 token
     * @return 支付宝支付页面
     */
    @Override
    public String getAlipayPage(String orderId, String token) {
        OrderInfo orderInfo = this.baseMapper.selectById(orderId);
        if(orderInfo == null || !orderInfo.getStatus().equals(OrderStatus.UNPAID)){
            throw new OrderOverTimeException("订单超时");
        }

        String key = Constant.PRODUCT_REDIS_PREFIX + "order:" + StpUtil.getLoginIdByToken(token) + orderId + ":pay_page";
        String alipayPayPage = stringRedisTemplate.opsForValue().get(key);
        if(!StringUtils.hasText(alipayPayPage)){
            throw new OrderOverTimeException("订单超时");
        }

        return alipayPayPage;
    }

    /**
     * 根据订单号获取订单信息，用于检查订单状态
     * @param orderId 订单号
     * @return 订单信息
     */
    @Override
    public OrderInfo getOrderInfoById(String orderId) {
        return this.baseMapper.selectById(orderId);
    }
}
