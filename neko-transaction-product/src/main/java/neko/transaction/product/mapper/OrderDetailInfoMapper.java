package neko.transaction.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.entity.OrderDetailInfo;
import neko.transaction.product.vo.OrderDetailInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 分页查询卖家自身的订单详情信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @param sellerUid 卖家学号
     * @param status 订单详情状态
     * @return 查询结果
     */
    List<OrderDetailInfoVo> sellerSelfPageQuery(Integer limited,
                                              Integer start,
                                              String queryWords,
                                              String sellerUid,
                                              Byte status);

    /**
     * 分页查询卖家自身的订单详情信息的结果总数量
     * @param queryWords 查询条件
     * @param sellerUid 卖家学号
     * @param status 订单详情状态
     * @return 查询结果的结果总数量
     */
    int sellerSelfPageQueryNumber(String queryWords,
                                  String sellerUid,
                                  Byte status);

    /**
     * 用户已收货的订单详情信息是否存在
     * @param orderId 订单号
     * @param productId 商品id
     * @param uid 学号
     * @return 用户已收货的订单详情信息是否存在
     */
    boolean isReceivedOrderDetailInfoExist(String orderId, String productId, String uid);
}
