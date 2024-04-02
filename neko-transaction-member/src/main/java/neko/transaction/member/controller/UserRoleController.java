package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.entity.UserRole;
import neko.transaction.member.service.UserRoleService;
import neko.transaction.member.vo.NewUserRoleVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@RestController
@RequestMapping("user_role")
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;

    /**
     * 管理员添加角色
     * @param vo 添加角色vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PutMapping("new_user_role")
    public ResultObject<Object> newUserRole(@Validated @RequestBody NewUserRoleVo vo){
        userRoleService.newUserRole(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询角色信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("role_info")
    public ResultObject<Page<UserRole>> roleInfo(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(userRoleService.getUserRolesByQueryLimitedPage(vo));
    }

    /**
     * 管理员获取管理员角色
     * @return 管理员角色
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("admin_role_info")
    public ResultObject<List<UserRole>> adminRoleInfo(){
        return ResultObject.ok(userRoleService.getAdminRoles());
    }

    /**
     * 管理员根据 角色id 删除角色
     * @param roleId 角色id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @DeleteMapping("delete_role")
    public ResultObject<Object> deleteRole(@RequestParam Integer roleId){
        userRoleService.deleteUserRole(roleId);

        return ResultObject.ok();
    }
}
