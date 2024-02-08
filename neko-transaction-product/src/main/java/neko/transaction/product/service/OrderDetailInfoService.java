package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.OrderDetailInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.OrderDetailInfoVo;

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
}
