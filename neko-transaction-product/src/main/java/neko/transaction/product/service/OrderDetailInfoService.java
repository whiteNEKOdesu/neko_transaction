package neko.transaction.product.service;

import neko.transaction.product.entity.OrderDetailInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
