package neko.transaction.member.service.impl;

import neko.transaction.member.entity.AdminInfo;
import neko.transaction.member.mapper.AdminInfoMapper;
import neko.transaction.member.service.AdminInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo> implements AdminInfoService {

}
