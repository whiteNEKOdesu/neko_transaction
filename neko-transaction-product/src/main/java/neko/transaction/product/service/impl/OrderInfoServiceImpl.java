package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.product.entity.OrderInfo;
import neko.transaction.product.mapper.OrderInfoMapper;
import neko.transaction.product.service.OrderInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成 token 保证创建订单接口幂等性，用于创建订单传入
     * @return 创建订单传入的 token
     */
    @Override
    public String getPreOrderToken() {
        //生成 token 保证创建订单接口幂等性，用于创建订单传入
        String token = IdUtil.randomUUID();
        String key = Constant.PRODUCT_REDIS_PREFIX + "order_id:" + StpUtil.getLoginId().toString() + ":" + token;

        stringRedisTemplate.opsForValue().setIfAbsent(key,
                token,
                1000 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return token;
    }
}
