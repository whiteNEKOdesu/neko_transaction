package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.entity.UserWeight;
import neko.transaction.member.service.UserWeightService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("user_weight")
public class UserWeightController {
    @Resource
    private UserWeightService userWeightService;

    /**
     * 管理员添加权限
     * @param weightType 权限名
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PutMapping("new_user_weight")
    public ResultObject<Object> newUserWeight(@RequestParam String weightType){
        userWeightService.newUserWeight(weightType);

        return ResultObject.ok();
    }

    /**
     * 管理员分页查询普通权限信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("weight_info")
    public ResultObject<Page<UserWeight>> weightInfo(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(userWeightService.getUserWeightByQueryLimitedPage(vo));
    }

    /**
     * 管理员查询指定 角色id 未绑定的权限
     * @param roleId 角色id
     * @return 角色id 未绑定的权限
     */
    @SaCheckRole(RoleType.ROOT)
    @SaCheckLogin
    @PostMapping("unbind_weight_info")
    public ResultObject<List<UserWeight>> unbindWeightInfo(@RequestParam Integer roleId){
        return ResultObject.ok(userWeightService.getUnbindWeightByRoleId(roleId));
    }

    /**
     * 管理员删除指定 权限id 的权限
     * @param weightId 权限id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @DeleteMapping("delete_weight")
    public ResultObject<Object> deleteWeight(@RequestParam Integer weightId){
        userWeightService.deleteUserWeight(weightId);

        return ResultObject.ok();
    }
}
