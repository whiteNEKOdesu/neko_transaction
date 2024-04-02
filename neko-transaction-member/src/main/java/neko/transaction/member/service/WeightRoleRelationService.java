package neko.transaction.member.service;

import neko.transaction.member.entity.WeightRoleRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.NewWeightRoleRelationVo;

import java.util.List;

/**
 * <p>
 * 权限，角色关系表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface WeightRoleRelationService extends IService<WeightRoleRelation> {
    /**
     * 根据用户id获取用户角色，权限信息
     * @param uid 用户id
     * @return 用户角色，权限信息
     */
    List<WeightRoleRelation> getRelations(String uid);

    /**
     * 根据用户id获取用户权限信息
     * @param uid 用户id
     * @return 用户权限信息
     */
    List<String> getWeightTypesByUid(String uid);

    /**
     * 根据用户id获取用户角色信息
     * @param uid 用户id
     * @return 用户角色信息
     */
    List<String> getRoleTypesByUid(String uid);

    /**
     * 根绝 角色id 获取权限，角色关系
     * @param roleId 角色id
     * @return 权限，角色关系
     */
    List<WeightRoleRelation> getRelationsByRoleId(Integer roleId);

    /**
     * 批量添加权限，角色关系
     * @param vo 添加权限，角色关系vo
     */
    void newRelations(NewWeightRoleRelationVo vo);
}
