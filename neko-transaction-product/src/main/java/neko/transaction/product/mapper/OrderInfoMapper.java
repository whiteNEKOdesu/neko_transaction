package neko.transaction.product.mapper;

import neko.transaction.product.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    /**
     * 根据订单号将订单状态修改为取消状态
     * @param orderId 订单号
     */
    void updateOrderInfoStatusToCancel(String orderId);
}
