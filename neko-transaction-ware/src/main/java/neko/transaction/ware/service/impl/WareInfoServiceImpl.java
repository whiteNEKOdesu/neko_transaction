package neko.transaction.ware.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.StockStatus;
import neko.transaction.commonbase.utils.exception.ProductServiceException;
import neko.transaction.commonbase.utils.exception.StockNotEnoughException;
import neko.transaction.ware.entity.StockLockLog;
import neko.transaction.ware.entity.WareInfo;
import neko.transaction.ware.feign.product.OrderInfoFeignService;
import neko.transaction.ware.feign.product.ProductInfoFeignService;
import neko.transaction.ware.mapper.WareInfoMapper;
import neko.transaction.ware.service.StockLockLogService;
import neko.transaction.ware.service.WareInfoService;
import neko.transaction.ware.to.OrderInfoTo;
import neko.transaction.ware.to.ProductInfoTo;
import neko.transaction.ware.vo.LockStockVo;
import neko.transaction.ware.vo.WareInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 库存信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Service
@Slf4j
public class WareInfoServiceImpl extends ServiceImpl<WareInfoMapper, WareInfo> implements WareInfoService {
    @Resource
    private StockLockLogService stockLockLogService;

    @Resource
    private ProductInfoFeignService productInfoFeignService;

    @Resource
    private OrderInfoFeignService orderInfoFeignService;

    /**
     * 根据商品id获取库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    @Override
    public WareInfoVo wareInfoById(String productId) {
        return this.baseMapper.wareInfoById(productId);
    }

    /**
     * 添加库存信息
     * @param productId 商品id
     * @param stock 库存数量
     */
    @Override
    public void newWareInfo(String productId, Integer stock) {
        WareInfo wareInfo = new WareInfo();
        wareInfo.setProductId(productId)
                .setStock(stock);

        this.baseMapper.insert(wareInfo);
    }

    /**
     * 修改库存数量
     * @param productId 商品id
     * @param offset 库存偏移量
     */
    @Override
    public void updateStock(String productId, int offset) {
        String token = StpUtil.getTokenValue();
        ResultObject<ProductInfoTo> r = productInfoFeignService.userSelfProductById(productId, token);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }
        if(r.getResult() == null){
            throw new NotPermissionException("商品不属于此用户");
        }

        //修改库存数量
        if(this.baseMapper.updateStockByProductId(productId, offset) != 1){
            throw new StockNotEnoughException("库存修改后小于0");
        }
    }

    /**
     * 锁定库存
     * @param vo 锁定库存vo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockStock(LockStockVo vo) {
        //订单号
        String orderId = vo.getOrderId();
        //要锁定的库存的商品信息
        List<LockStockVo.LockProductInfo> lockProductInfos = vo.getLockProductInfos();

        //step1 -> 锁定对应商品的库存
        if(lockProductInfos.size() != this.baseMapper.lockStocks(lockProductInfos)){
            throw new StockNotEnoughException("库存不足");
        }

        //step2 -> 将库存锁定记录信息添加到库存锁定日志表
        stockLockLogService.insertBatch(orderId, lockProductInfos);
    }

    /**
     * 根据订单号解锁库存
     * @param orderId 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockStock(String orderId) {
        log.info("订单超时准备解锁库存，订单号: " + orderId);

        //远程调用商品微服务获取订单信息
        ResultObject<OrderInfoTo> r = orderInfoFeignService.remoteInvokeOrderInfoById(orderId);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }

        //获取订单信息
        OrderInfoTo orderInfo = r.getResult();

        if(orderInfo == null){
            log.warn("解锁库存订单号: " + orderId + "，订单不存在，尝试解锁库存");
            //解锁库存
            unlockStockTask(orderId);

            return;
        }

        //解锁库存
        unlockStockTask(orderId);
    }

    /**
     * 解锁指定订单号涉及的库存并扣除库存，用于确认支付后扣除库存
     * @param orderId 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmLockStockPaid(String orderId) {
        log.info("订单支付确认准备扣除库存，订单号: " + orderId);

        //获取订单锁定库存记录信息
        List<StockLockLog> stockLockLogs = stockLockLogService.lambdaQuery().eq(StockLockLog::getOrderId, orderId)
                .eq(StockLockLog::getStatus, StockStatus.LOCKING)
                .list();
        if(stockLockLogs == null || stockLockLogs.isEmpty()){
            log.warn("订单号: " + orderId + "对应库存锁定记录不存在");

            return;
        }

        for(StockLockLog stockLockLog : stockLockLogs){
            //解锁库存并扣除库存
            this.baseMapper.decreaseStock(stockLockLog.getWareId(),
                    stockLockLog.getLockNumber());
        }

        //修改库存锁定记录状态为已支付
        stockLockLogService.updateStatusToPaid(orderId);

        log.info("订单支付确认扣除库存完成，订单号: " + orderId);
    }

    /**
     * 解锁库存任务
     * @param orderId 订单号
     */
    private void unlockStockTask(String orderId){
        //获取订单锁定库存记录信息
        List<StockLockLog> stockLockLogs = stockLockLogService.lambdaQuery().eq(StockLockLog::getOrderId, orderId)
                .eq(StockLockLog::getStatus, StockStatus.LOCKING)
                .list();
        if(stockLockLogs == null || stockLockLogs.isEmpty()){
            log.warn("订单号: " + orderId + "对应库存锁定记录不存在");

            return;
        }

        for(StockLockLog stockLockLog : stockLockLogs){
            //解锁库存
            this.baseMapper.unlockStock(stockLockLog.getWareId(),
                    stockLockLog.getStockLockLogId());
        }

        //修改库存锁定记录状态为已解锁
        stockLockLogService.updateStatusToCancelLock(orderId);

        log.info("订单超时解锁库存完成，订单号: " + orderId);
    }
}
