package neko.transaction.member.service.impl;

import neko.transaction.member.entity.UserRoleRelation;
import neko.transaction.member.mapper.UserRoleRelationMapper;
import neko.transaction.member.service.UserRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户，角色关系表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements UserRoleRelationService {

}
