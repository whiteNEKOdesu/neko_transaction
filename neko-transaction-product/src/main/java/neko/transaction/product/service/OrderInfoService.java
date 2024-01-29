package neko.transaction.product.service;

import neko.transaction.product.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 生成 token 保证创建订单接口幂等性，用于创建订单传入
     * @return 创建订单传入的 token
     */
    String getPreOrderToken();
}
