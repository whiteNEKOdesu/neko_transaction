package neko.transaction.ware.service.impl;

import neko.transaction.ware.entity.StockLockLog;
import neko.transaction.ware.mapper.StockLockLogMapper;
import neko.transaction.ware.service.StockLockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存锁定日志表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Service
public class StockLockLogServiceImpl extends ServiceImpl<StockLockLogMapper, StockLockLog> implements StockLockLogService {

    /**
     * 根据订单号将库存锁定日志状态修改为已解锁
     * @param orderId 订单号
     */
    @Override
    public void updateStatusToCancelLock(String orderId) {
        this.baseMapper.updateStatusToCancelLock(orderId);
    }

    /**
     * 根据订单号将库存锁定日志状态修改为已支付
     * @param orderId 订单号
     */
    @Override
    public void updateStatusToPaid(String orderId) {
        this.baseMapper.updateStatusToPaid(orderId);
    }
}
