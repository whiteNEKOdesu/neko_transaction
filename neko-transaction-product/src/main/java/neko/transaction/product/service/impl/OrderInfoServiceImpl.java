package neko.transaction.product.service.impl;

import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.mapper.OrderInfoMapper;
import neko.transaction.product.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

}
