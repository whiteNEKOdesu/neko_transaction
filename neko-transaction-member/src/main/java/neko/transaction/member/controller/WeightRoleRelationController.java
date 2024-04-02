package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.entity.WeightRoleRelation;
import neko.transaction.member.service.WeightRoleRelationService;
import neko.transaction.member.vo.NewWeightRoleRelationVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限，角色关系表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("weight_role_relation")
public class WeightRoleRelationController {
    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    /**
     * 管理员批量添加权限，角色关系
     * @param vo 批量添加权限，角色关系vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PutMapping("new_relations")
    public ResultObject<Object> newRelations(@Validated @RequestBody NewWeightRoleRelationVo vo){
        weightRoleRelationService.newRelations(vo);

        return ResultObject.ok();
    }

    /**
     * 根绝 角色id 获取权限，角色关系
     * @param roleId 角色id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("relation_info_by_role_id")
    public ResultObject<List<WeightRoleRelation>> relationInfoByRoleId(@RequestParam Integer roleId){
        return ResultObject.ok(weightRoleRelationService.getRelationsByRoleId(roleId));
    }

    /**
     * 根据用户id获取用户角色信息，建议只提供给微服务远程调用
     * @param uid 用户id
     * @return 响应结果
     */
    @PostMapping("relation_info_by_uid")
    public ResultObject<List<WeightRoleRelation>> relationInfoByUid(@RequestParam String uid){
        return ResultObject.ok(weightRoleRelationService.getRelations(uid));
    }
}
