package neko.transaction.member.service.impl;

import neko.transaction.member.entity.ApiAuthInfo;
import neko.transaction.member.mapper.ApiAuthInfoMapper;
import neko.transaction.member.service.ApiAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * api鉴权信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
@Service
public class ApiAuthInfoServiceImpl extends ServiceImpl<ApiAuthInfoMapper, ApiAuthInfo> implements ApiAuthInfoService {

}
