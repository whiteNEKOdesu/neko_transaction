package neko.transaction.product.mapper;

import neko.transaction.product.entity.OrderDetailInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Mapper
public interface OrderDetailInfoMapper extends BaseMapper<OrderDetailInfo> {
    /**
     * 根据订单详情id，学号获取订单详情信息
     * @param orderDetailId 订单详情id
     * @param uid 学号
     * @return 订单详情信息
     */
    OrderDetailInfo getByIdUid(String orderDetailId, String uid);
}
