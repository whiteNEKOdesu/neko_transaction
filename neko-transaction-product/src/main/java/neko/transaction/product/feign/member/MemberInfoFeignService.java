package neko.transaction.product.feign.member;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.product.to.AddMemberBalanceTo;
import neko.transaction.product.to.MemberInfoTo;
import neko.transaction.product.to.PublicUserInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 根据学号获取用户公开信息
     * @param uid 学号
     * @return 用户公开信息
     */
    @GetMapping("member_info/public_member_info")
    ResultObject<PublicUserInfoTo> publicMemberInfo(@RequestParam String uid);

    /**
     * 添加用户余额
     * @param to 添加用户余额to
     * @return 响应结果
     */
    @PostMapping("member_info/add_balance")
    ResultObject<Object> addBalance(@RequestBody AddMemberBalanceTo to);
}
