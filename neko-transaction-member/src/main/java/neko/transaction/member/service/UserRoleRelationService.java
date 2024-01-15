package neko.transaction.member.service;

import neko.transaction.member.entity.UserRoleRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户，角色关系表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface UserRoleRelationService extends IService<UserRoleRelation> {
    /**
     * 根据用户id获取用户的角色id
     * @param uid 用户id
     * @return 用户的角色id List集合
     */
    List<Integer> getUserRoleIds(String uid);
}
