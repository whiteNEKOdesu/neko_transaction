package neko.transaction.product.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.json.JSONUtil;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.product.feign.member.WeightRoleRelationFeignService;
import neko.transaction.product.to.WeightRoleRelationTo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限设置配置
 */
@Component
public class SAWeightConfig implements StpInterface {
    @Resource
    private WeightRoleRelationFeignService weightRoleRelationFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return getWeightTypesByUid(o.toString());
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return getRoleTypesByUid(o.toString());
    }

    private List<String> getWeightTypesByUid(String uid){
        return getRelations(uid).stream().filter(Objects::nonNull)
                .map(WeightRoleRelationTo::getWeightType)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getRoleTypesByUid(String uid){
        return getRelations(uid).stream().filter(Objects::nonNull)
                .map(WeightRoleRelationTo::getRoleType)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据用户id获取用户角色，权限信息
     * @param uid 用户id
     * @return 用户角色，权限信息
     */
    public List<WeightRoleRelationTo> getRelations(String uid) {
        String key = Constant.MEMBER_REDIS_PREFIX + "weight_cache:" + uid;
        String relationCache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(relationCache != null){
            return JSONUtil.toList(JSONUtil.parseArray(relationCache), WeightRoleRelationTo.class);
        }

        ResultObject<List<WeightRoleRelationTo>> r = weightRoleRelationFeignService.relationInfoByUid(uid);
        if(r.getResponseCode() != 200){
            throw new MemberServiceException("member微服务远程调用错误");
        }

        return r.getResult();
    }
}
