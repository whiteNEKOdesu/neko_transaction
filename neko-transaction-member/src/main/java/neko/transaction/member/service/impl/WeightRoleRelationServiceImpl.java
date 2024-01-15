package neko.transaction.member.service.impl;

import cn.hutool.json.JSONUtil;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.member.entity.WeightRoleRelation;
import neko.transaction.member.mapper.WeightRoleRelationMapper;
import neko.transaction.member.service.UserRoleRelationService;
import neko.transaction.member.service.UserWeightService;
import neko.transaction.member.service.WeightRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限，角色关系表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class WeightRoleRelationServiceImpl extends ServiceImpl<WeightRoleRelationMapper, WeightRoleRelation> implements WeightRoleRelationService {
    @Resource
    private UserRoleRelationService userRoleRelationService;

    @Resource
    private UserWeightService userWeightService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据用户id获取用户角色，权限信息
     * @param uid 用户id
     * @return 用户角色，权限信息
     */
    @Override
    public List<WeightRoleRelation> getRelations(String uid) {
        String key = Constant.MEMBER_REDIS_PREFIX + "weight_cache:" + uid;
        String relationCache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(relationCache != null){
            return JSONUtil.toList(JSONUtil.parseArray(relationCache), WeightRoleRelation.class);
        }

        List<WeightRoleRelation> relations = this.baseMapper.getRelationsByRoleIds(userRoleRelationService.getUserRoleIds(uid));
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(relations),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return relations;
    }

    /**
     * 根据用户id获取用户权限信息
     * @param uid 用户id
     * @return 用户权限信息
     */
    @Override
    public List<String> getWeightTypesByUid(String uid) {
        return getRelations(uid).stream().filter(Objects::nonNull)
                .map(WeightRoleRelation::getWeightType)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 根据用户id获取用户角色信息
     * @param uid 用户id
     * @return 用户角色信息
     */
    @Override
    public List<String> getRoleTypesByUid(String uid) {
        return getRelations(uid).stream().filter(Objects::nonNull)
                .map(WeightRoleRelation::getRoleType)
                .distinct()
                .collect(Collectors.toList());
    }
}
