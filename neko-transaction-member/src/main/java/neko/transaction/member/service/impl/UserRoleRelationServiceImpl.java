package neko.transaction.member.service.impl;

import neko.transaction.member.entity.UserRoleRelation;
import neko.transaction.member.mapper.UserRoleRelationMapper;
import neko.transaction.member.service.UserRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 根据用户id获取用户的角色id
     * @param uid 用户id
     * @return 用户的角色id List集合
     */
    @Override
    public List<Integer> getUserRoleIds(String uid) {
        return this.baseMapper.getRoleIdsByUid(uid);
    }

    /**
     * 批量为用户添加角色关联
     * @param uid 学号
     * @param roleIds 角色id List集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void newRelations(String uid, List<Integer> roleIds) {
        List<UserRoleRelation> relations = roleIds.stream().filter(Objects::nonNull)
                .distinct()
                .map(r -> new UserRoleRelation().setUid(uid)
                        .setRoleId(r))
                .collect(Collectors.toList());

        this.saveBatch(relations);
    }
}
