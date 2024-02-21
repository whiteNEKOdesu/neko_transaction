package neko.transaction.product.mapper;

import neko.transaction.product.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.OrderInfoPageQueryVo;
import neko.transaction.product.vo.OrderStatusAggCountVo;
import neko.transaction.product.vo.OrderTransactionInYearVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 分页查询学生自身的订单信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @param uid 学号
     * @param status 商品订单状态
     * @return 查询结果
     */
    List<OrderInfoPageQueryVo> userSelfPageQuery(Integer limited,
                                                 Integer start,
                                                 String queryWords,
                                                 String uid,
                                                 Byte status);

    /**
     * 分页查询学生自身的订单信息的结果总数量
     * @param queryWords 查询条件
     * @param uid 学号
     * @param status 商品订单状态
     * @return 查询结果的结果总数量
     */
    int userSelfPageQueryNumber(String queryWords,
                                String uid,
                                Byte status);

    /**
     * 获取订单按照状态聚合信息
     * @return 订单按照状态聚合信息
     */
    List<OrderStatusAggCountVo> statusAggCount();

    /**
     * 获取指定年的订单流水信息
     * @param year 指定年
     * @return 订单流水信息
     */
    List<OrderTransactionInYearVo> transactionInYear(LocalDateTime year);
}
