package neko.transaction.product.feign.member;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.product.to.MemberInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 用户微服务用户信息远程调用
 */
@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "MemberInfo")
public interface MemberInfoFeignService {
    /**
     * 获取用户自身的详细信息
     * @param token 用户的 token
     * @return 用户自身的详细信息
     */
    @GetMapping("member_info/user_self_info")
    ResultObject<MemberInfoTo> userSelfInfo(@RequestHeader("neko_transaction") String token);
}
