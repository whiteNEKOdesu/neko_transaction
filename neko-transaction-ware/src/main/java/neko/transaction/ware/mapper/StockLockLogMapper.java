package neko.transaction.ware.mapper;

import neko.transaction.ware.entity.StockLockLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.ware.vo.LockStockVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 库存锁定日志表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Mapper
public interface StockLockLogMapper extends BaseMapper<StockLockLog> {
    /**
     * 根据订单号将库存锁定日志状态修改为已解锁
     * @param orderId 订单号
     */
    void updateStatusToCancelLock(String orderId);

    /**
     * 根据订单号将库存锁定日志状态修改为已支付
     * @param orderId 订单号
     */
    void updateStatusToPaid(String orderId);

    /**
     * 批量添加库存锁定记录
     * @param orderId 订单号
     * @param lockProductInfos 锁定的库存信息
     */
    void insertBatch(String orderId,
                     List<LockStockVo.LockProductInfo> lockProductInfos);
}
