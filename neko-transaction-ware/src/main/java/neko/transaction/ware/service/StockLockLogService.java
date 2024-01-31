package neko.transaction.ware.service;

import neko.transaction.ware.entity.StockLockLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 库存锁定日志表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
public interface StockLockLogService extends IService<StockLockLog> {
    /**
     * 根据订单号将库存锁定日志状态修改为已解锁
     * @param orderId 订单号
     */
    void updateStatusToCancelLock(String orderId);
}
