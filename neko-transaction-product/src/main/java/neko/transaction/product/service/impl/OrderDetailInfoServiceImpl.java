package neko.transaction.product.service.impl;

import neko.transaction.product.entity.OrderDetailInfo;
import neko.transaction.product.mapper.OrderDetailInfoMapper;
import neko.transaction.product.service.OrderDetailInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@Service
public class OrderDetailInfoServiceImpl extends ServiceImpl<OrderDetailInfoMapper, OrderDetailInfo> implements OrderDetailInfoService {

}
