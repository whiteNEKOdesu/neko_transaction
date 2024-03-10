package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.*;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.commonbase.utils.exception.OrderOverTimeException;
import neko.transaction.commonbase.utils.exception.StockNotEnoughException;
import neko.transaction.commonbase.utils.exception.WareServiceException;
import neko.transaction.product.config.AliPayTemplate;
import neko.transaction.product.entity.OrderDetailInfo;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.feign.ware.WareInfoFeignService;
import neko.transaction.product.mapper.OrderInfoMapper;
import neko.transaction.product.service.OrderDetailInfoService;
import neko.transaction.product.service.OrderInfoService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.service.PurchaseListService;
import neko.transaction.product.to.AliPayTo;
import neko.transaction.product.to.LockStockTo;
import neko.transaction.product.to.NewOrderRedisTo;
import neko.transaction.product.to.RabbitMQMessageTo;
import neko.transaction.product.vo.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private WareInfoFeignService wareInfoFeignService;

    @Resource
    private OrderDetailInfoService orderDetailInfoService;

    @Resource
    private PurchaseListService purchaseListService;

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
        CompletableFuture.runAsync(() -> {
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
        }, threadPool);

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

        //step3.1.2 -> 异步将商品信息记录到 redis 中
        CompletableFuture<Void> redisLogTask = getProductInfoTask.thenAcceptAsync((productInfoMap -> {
            if (productInfoMap == null || productInfoMap.isEmpty()) {
                isException.set(true);

                return;
            }

            NewOrderRedisTo newOrderRedisTo = new NewOrderRedisTo();
            //设置订单号
            newOrderRedisTo.setOrderId(orderId)
                    //设置是否从购物车中提交
                    .setIsFromPurchaseList(vo.getIsFromPurchaseList());
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

            String productInfoKey = Constant.PRODUCT_REDIS_PREFIX + "order:" + orderId + ":product_info";
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

        //step3.2 -> 异步锁定库存
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

        CompletableFuture.allOf(calculateCostTask, redisLogTask, lockStockTask).get();

        if(isException.get()){
            throw new StockNotEnoughException("库存不足");
        }

        BigDecimal cost = calculateCostTask.get();
        if (cost == null) {
            throw new StockNotEnoughException("库存不足");
        }

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId)
                .setUid(uid)
                .setCost(cost)
                .setActualCost(cost);

        //记录订单信息到订单表
        this.baseMapper.insert(orderInfo);

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
        String key = Constant.PRODUCT_REDIS_PREFIX + "order:" + StpUtil.getLoginIdByToken(token) + orderId + ":pay_page";
        //获取 redis 缓存
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return cache;
        }

        OrderInfo orderInfo = this.baseMapper.selectById(orderId);
        if(orderInfo == null || !orderInfo.getStatus().equals(OrderStatus.UNPAID)){
            throw new OrderOverTimeException("订单超时");
        }

        AliPayTo aliPayTo = new AliPayTo();
        aliPayTo.setOut_trade_no(orderId)
                .setSubject("NEKO_TRANSACTION")
                //设置折扣价格
                .setTotal_amount(orderInfo.getActualCost().toString())
                .setBody("NEKO_TRANSACTION");
        String page;
        //将支付宝支付页面信息存入 redis
        try {
            page = aliPayTemplate.pay(aliPayTo);

            stringRedisTemplate.opsForValue().setIfAbsent(key,
                    page,
                    1000 * 60 * 4,
                    TimeUnit.MILLISECONDS);
        }catch (AlipayApiException e){
            e.printStackTrace();

            return null;
        }

        return page;
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

    /**
     * 根据订单号将订单状态修改为取消状态
     * @param orderId 订单号
     */
    @Override
    public void updateOrderInfoStatusToCancel(String orderId) {
        this.baseMapper.updateOrderInfoStatusToCancel(orderId);
    }

    /**
     * 支付宝支付成功异步通知处理
     * @param vo 支付宝支付成功异步通知vo
     * @param request HttpServletRequest
     * @return 向支付宝响应的处理结果
     */
    @Override
    public String alipayTradeCheck(AliPayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        //验签
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                aliPayTemplate.getAlipayPublicKey(),
                aliPayTemplate.getCharset(),
                aliPayTemplate.getSignType());

        if(signVerified){
            if(vo.getTrade_status().equals("TRADE_SUCCESS") || vo.getTrade_status().equals("TRADE_FINISHED")){
                //获取订单号
                String orderId = vo.getOut_trade_no();
                //获取订单信息
                OrderInfo orderInfo = this.baseMapper.selectById(orderId);
                if(orderInfo == null){
                    log.error("订单号: " + orderId + "，支付宝流水号: " + vo.getTrade_no() + "，订单不存在");

                    return "error";
                }

                //step1 -> 修改订单状态信息为已支付状态
                OrderInfo todoUpdate = new OrderInfo();
                todoUpdate.setOrderId(orderId)
                        .setAlipayTradeId(vo.getTrade_no())
                        .setStatus(OrderStatus.PAID);

                //修改订单状态信息
                this.baseMapper.updateById(todoUpdate);

                //step2 -> 获取订单涉及的商品信息，并添加到订单详情表
                String productInfoKey = Constant.PRODUCT_REDIS_PREFIX + "order:" + orderId + ":product_info";
                //获取订单涉及的商品信息
                NewOrderRedisTo newOrderRedisTo = JSONUtil.toBean(stringRedisTemplate.opsForValue().get(productInfoKey), NewOrderRedisTo.class);

                //map 映射关系: productId -> NewOrderRedisTo.ProductsInOrder
                Map<String,NewOrderRedisTo.ProductsInOrder> productInfoMap = new HashMap<>();
                for(NewOrderRedisTo.ProductsInOrder productsInOrder : newOrderRedisTo.getProductsInOrders()){
                    productInfoMap.put(productsInOrder.getProductId(), productsInOrder);
                }
                List<String> productIds = new ArrayList<>(productInfoMap.keySet());
                //获取商品详情信息
                List<ProductDetailInfoVo> productDetailInfo = productInfoService.getProductDetailInfoByIds(productIds);
                //添加到订单详情表的集合
                List<OrderDetailInfo> todoAdds = new ArrayList<>();
                for(ProductDetailInfoVo productDetailInfoVo : productDetailInfo){
                    //获取订单号
                    String productId = productDetailInfoVo.getProductId();
                    NewOrderRedisTo.ProductsInOrder productsInOrder = productInfoMap.get(productId);
                    OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
                    //设置订单详情信息
                    orderDetailInfo.setOrderId(orderId)
                            .setProductId(productId)
                            .setProductName(productDetailInfoVo.getProductName())
                            .setDisplayImage(productDetailInfoVo.getDisplayImage())
                            .setFullCategoryName(productDetailInfoVo.getFullCategoryName())
                            .setSellerUid(productDetailInfoVo.getUid())
                            .setCost(productsInOrder.getCost())
                            .setActualCost(productsInOrder.getActualCost())
                            .setNumber(productsInOrder.getNumber());

                    todoAdds.add(orderDetailInfo);
                }

                //将订单详情信息添加到订单详情表
                orderDetailInfoService.saveBatch(todoAdds);

                //step3 -> 异步添加对应商品销量
                CompletableFuture.runAsync(() -> {
                    for(NewOrderRedisTo.ProductsInOrder productsInOrder : newOrderRedisTo.getProductsInOrders()){
                        productInfoService.increaseSaleNumber(productsInOrder.getProductId(), productsInOrder.getNumber());
                    }
                }, threadPool).exceptionallyAsync(e -> {
                    e.printStackTrace();

                    return null;
                }, threadPool);

                //step4 -> 远程调用库存微服务解锁库存并扣除库存
                ResultObject<Object> r = wareInfoFeignService.confirmLockStockPaid(orderId);
                if(!r.getResponseCode().equals(200)){
                    throw new WareServiceException("ware微服务远程调用异常");
                }

                //step5 -> 如果订单从购物车提交，则删除购物车中对应的商品
                if(newOrderRedisTo.getIsFromPurchaseList()){
                    purchaseListService.deleteByIds(productIds, orderInfo.getUid());
                }

                log.info("订单号: " + orderId + "，支付宝流水号: " + vo.getTrade_no() + "，订单支付确认完成");
            }

            return "success";
        }else{
            return "error";
        }
    }

    /**
     * 根据订单号获取用户自身的订单信息
     * @param orderId 订单号
     * @return 用户自身的订单信息
     */
    @Override
    public OrderInfo getUserSelfOrderInfoByOrderId(String orderId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderInfo::getOrderId, orderId)
                .eq(OrderInfo::getUid, StpUtil.getLoginId().toString())
                .ne(OrderInfo::getStatus, OrderStatus.CANCELED);

        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页查询学生自身的订单信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<OrderInfoPageQueryVo> userSelfPageQuery(QueryVo vo) {
        Page<OrderInfoPageQueryVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = StpUtil.getLoginId().toString();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.userSelfPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.userSelfPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }

    /**
     * 获取订单按照状态聚合信息
     * @return 订单按照状态聚合信息
     */
    @Override
    public List<OrderStatusAggCountVo> statusAggCount() {
        return this.baseMapper.statusAggCount();
    }

    /**
     * 获取指定年的订单流水信息
     * @param year 指定年
     * @return 订单流水信息
     */
    @Override
    public List<OrderTransactionInYearVo> transactionInYear(LocalDateTime year) {
        return this.baseMapper.transactionInYear(LocalDateTime.of(year.getYear(),
                1,
                1,
                0,
                0,
                0));
    }
}
