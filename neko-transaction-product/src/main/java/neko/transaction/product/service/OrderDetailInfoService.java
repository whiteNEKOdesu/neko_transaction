package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.OrderDetailInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.CensorReturnApplyVo;
import neko.transaction.product.vo.NewReturnApplyVo;
import neko.transaction.product.vo.OrderDetailInfoVo;
import neko.transaction.product.vo.OrderDetailStatusAggVo;

import java.util.List;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
public interface OrderDetailInfoService extends IService<OrderDetailInfo> {
    /**
     * 学生确认收货
     * @param orderDetailId 订单详情id
     */
    void confirmReceived(String orderDetailId);

    /**
     * 分页查询卖家自身的订单详情信息
     * @param vo 查询vo
     * @return 查询结果
     */
    Page<OrderDetailInfoVo> sellerSelfPageQuery(QueryVo vo);

    /**
     * 用户已收货的订单详情信息是否存在
     * @param orderId 订单号
     * @param productId 商品id
     * @param uid 学号
     * @return 用户已收货的订单详情信息是否存在
     */
    boolean isReceivedOrderDetailInfoExist(String orderId, String productId, String uid);

    /**
     * 获取订单详情按照状态聚合信息
     * @return 订单详情按照状态聚合信息
     */
    List<OrderDetailStatusAggVo> statusAggCount();

    /**
     * 添加退货申请
     * @param vo 添加退货申请vo
     */
    void newReturnApplyInfo(NewReturnApplyVo vo);

    /**
     * 卖家审核退货申请
     * @param vo 提交审核退货申请vo
     */
    void sellerCensorReturnApply(CensorReturnApplyVo vo);

    /**
     * 管理员审核退货申请
     * @param vo 提交审核退货申请vo
     */
    void adminCensorReturnApply(CensorReturnApplyVo vo);

    /**
     * 确认退货货物送达
     * @param applyId 申请id
     */
    void confirmReturnCargoSentBack(Long applyId);
}
