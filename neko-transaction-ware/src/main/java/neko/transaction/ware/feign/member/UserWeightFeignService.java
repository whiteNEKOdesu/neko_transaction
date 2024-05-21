package neko.transaction.ware.feign.member;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.ware.to.UserWeightTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户微服务权限信息远程调用
 */
@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "UserWeight")
public interface UserWeightFeignService {
    /**
     * 根绝 权限id 获取权限信息
     * @param weightId 权限id
     * @return 获取权限信息
     */
    @GetMapping("user_weight/user_weight_by_id")
    ResultObject<UserWeightTo> userWeightById(@RequestParam Integer weightId);
}
