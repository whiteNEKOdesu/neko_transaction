package neko.transaction.product.feign.member;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.product.to.UserRoleTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户微服务角色信息远程调用
 */
@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "UserRole")
public interface UserRoleFeignService {
    /**
     * 根据 角色id 获取角色信息
     * @param roleId 角色id
     * @return 角色信息
     */
    @GetMapping("user_role/role_info_by_id")
    ResultObject<UserRoleTo> roleInfoById(@RequestParam Integer roleId);
}
