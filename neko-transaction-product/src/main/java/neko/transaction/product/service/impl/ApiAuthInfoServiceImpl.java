package neko.transaction.product.service.impl;

import neko.transaction.product.entity.ApiAuthInfo;
import neko.transaction.product.mapper.ApiAuthInfoMapper;
import neko.transaction.product.service.ApiAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * api鉴权信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-05-20
 */
@Service
public class ApiAuthInfoServiceImpl extends ServiceImpl<ApiAuthInfoMapper, ApiAuthInfo> implements ApiAuthInfoService {

}
