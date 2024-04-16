package neko.transaction.product.feign.member;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.ServiceName;
import neko.transaction.product.to.WeightRoleRelationTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServiceName.MEMBER_SERVICE, contextId = "WeightRoleRelation")
public interface WeightRoleRelationFeignService {
    @PostMapping("weight_role_relation/relation_info_by_uid")
    ResultObject<List<WeightRoleRelationTo>> relationInfoByUid(@RequestParam String uid);
}
