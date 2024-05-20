package neko.transaction.member.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.member.entity.ApiAuthInfo;
import neko.transaction.member.entity.UserRole;
import neko.transaction.member.entity.UserWeight;
import neko.transaction.member.mapper.ApiAuthInfoMapper;
import neko.transaction.member.service.ApiAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.service.UserRoleService;
import neko.transaction.member.service.UserWeightService;
import neko.transaction.member.vo.UpdateApiAuthInfoVo;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * api鉴权信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
@Service
@Slf4j
public class ApiAuthInfoServiceImpl extends ServiceImpl<ApiAuthInfoMapper, ApiAuthInfo> implements ApiAuthInfoService {
    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserWeightService userWeightService;

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将 api 信息同步到数据库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronizeApiToDB() {
        //需要新同步的 api 信息 list
        List<ApiAuthInfo> newVersions = new ArrayList<>();

        //step1 -> 获取当前所有的请求路径信息
        Map<RequestMappingInfo, HandlerMethod> requestInfoMap = requestMappingHandlerMapping
                .getHandlerMethods();
        Set<String> pathSet = new HashSet<>();
        requestInfoMap.forEach((key, value) -> {
            ApiAuthInfo apiAuthInfo = new ApiAuthInfo();
            PathPatternsRequestCondition pathPatternsCondition = key.getPathPatternsCondition();
            if(pathPatternsCondition == null){
                return;
            }

            String path = pathPatternsCondition.getFirstPattern().toString();
            //api 路径去重
            if(pathSet.contains(path)){
                return;
            }
            pathSet.add(path);
            apiAuthInfo.setPath(path)
                    .setRequestMethod(key.getMethodsCondition().toString())
                    .setHandlerMethod(value.getMethod().getName());

            newVersions.add(apiAuthInfo);
        });

        //需要新同步的 api 信息的路径
        List<String> paths = newVersions.stream().map(ApiAuthInfo::getPath)
                .collect(Collectors.toList());

        //step2 -> 获取数据库中当前的 api 信息
        List<ApiAuthInfo> currentVersions = this.baseMapper.selectList(new QueryWrapper<ApiAuthInfo>().lambda()
                .in(ApiAuthInfo::getPath, paths));
        //将数据库中当前的 api 信息映射为 map
        Map<String, ApiAuthInfo> currentVersionMap = currentVersions.stream().collect(Collectors.toMap(ApiAuthInfo::getPath, apiAuthInfo -> apiAuthInfo));
        //获取数据库中当前的 api 信息的 id list
        List<Long> currentVersionIds = currentVersions.stream().map(ApiAuthInfo::getApiId).collect(Collectors.toList());

        //更新以及添加的 list
        List<ApiAuthInfo> todoUpdates = new ArrayList<>(), todoInserts = new ArrayList<>();

        //step3 -> 添加以及修改新同步的 api 信息
        for(ApiAuthInfo apiAuthInfo : newVersions){
            String path = apiAuthInfo.getPath();

            if(currentVersionMap.containsKey(apiAuthInfo.getPath())){
                //当前数据库存在对应的 api 信息，则修改
                ApiAuthInfo todoUpdate = new ApiAuthInfo();
                todoUpdate.setApiId(currentVersionMap.get(path).getApiId())
                        .setRequestMethod(apiAuthInfo.getRequestMethod())
                        .setHandlerMethod(apiAuthInfo.getHandlerMethod());

                todoUpdates.add(todoUpdate);
            }else{
                //当前数据库不存在对应的 api 信息，则添加
                ApiAuthInfo todoInsert = new ApiAuthInfo();
                todoInsert.setPath(path)
                        .setRequestMethod(apiAuthInfo.getRequestMethod())
                        .setHandlerMethod(apiAuthInfo.getHandlerMethod());

                todoInserts.add(todoInsert);
            }
        }

        int deleted = 0;
        if(!currentVersionIds.isEmpty()){
            //删除数据库中已被删除的 api 信息
            deleted = this.baseMapper.delete(new QueryWrapper<ApiAuthInfo>().lambda()
                    .notIn(ApiAuthInfo::getApiId, currentVersionIds));
        }
        if(!todoInserts.isEmpty()){
            //添加新的 api 信息
            this.baseMapper.insertBatch(todoInserts);
        }
        if(!todoUpdates.isEmpty()){
            //修改已有的 api 信息
            if(todoUpdates.size() != this.baseMapper.updateBatch(todoUpdates)){
                throw new IllegalArgumentException("修改新同步的 api 信息错误");
            }
        }

        String key = Constant.MEMBER_REDIS_PREFIX + "api_auth_infos";
        //删除权限缓存
        stringRedisTemplate.delete(key);

        log.info("api 信息同步成功，添加: " + todoInserts.size() + " 条，修改: " + todoUpdates.size() + " 条，删除: " + deleted + " 条");
    }

    /**
     * 分页查询 api 鉴权信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<ApiAuthInfo> pageQuery(QueryVo vo) {
        Page<ApiAuthInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<ApiAuthInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            //拼接查询条件
            queryWrapper.lambda().like(ApiAuthInfo::getPath, vo.getQueryWords())
                    .or()
                    .like(ApiAuthInfo::getHandlerMethod, vo.getQueryWords());
        }
        queryWrapper.lambda().orderByDesc(ApiAuthInfo::getApiId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 根据当前请求路径获取 api 鉴权信息
     * @return api 鉴权信息
     */
    @Override
    public ApiAuthInfo getApiAuthInfoByCurrentRequest() {
        String key = Constant.MEMBER_REDIS_PREFIX + "api_auth_infos";
        BoundHashOperations<String, Object, Object> boundHashOperations = stringRedisTemplate.boundHashOps(key);

        //获取合法的当前请求路径
        String requestURI = getValidateRequestUrl();

        //缓存有数据
        if(Boolean.TRUE.equals(boundHashOperations.hasKey(requestURI))){
            //获取 redis 缓存
            String cache = (String) boundHashOperations.get(requestURI);

            return JSONUtil.toBean(cache, ApiAuthInfo.class);
        }

        ApiAuthInfo apiAuthInfo = this.baseMapper.selectOne(new QueryWrapper<ApiAuthInfo>().lambda()
                .eq(ApiAuthInfo::getPath, requestURI));

        if(apiAuthInfo != null){
            //缓存无数据，将查询存入缓存
            boundHashOperations.put(requestURI, JSONUtil.toJsonStr(apiAuthInfo));
        }

        return apiAuthInfo;
    }

    /**
     * 修改 api 鉴权信息
     * @param vo 修改 api 鉴权信息vo
     */
    @Override
    public void updateApiAuthInfo(UpdateApiAuthInfoVo vo) {
        Integer roleId = vo.getRoleId(), weightId = vo.getWeightId();
        String role = null, weight = null;

        if(roleId != null){
            //获取角色信息
            UserRole userRole = userRoleService.getById(roleId);
            if(userRole == null){
                throw new NoSuchResultException("无此 roleId: " + roleId + " 的角色信息");
            }

            role = userRole.getRoleType();
        }
        if(weightId != null){
            //获取权限信息
            UserWeight userWeight = userWeightService.getById(weightId);
            if(userWeight == null){
                throw new NoSuchResultException("无此 weightId: " + weightId + " 的权限信息");
            }

            weight = userWeight.getWeightType();
        }

        //修改 api 鉴权信息
        this.baseMapper.updateApiAuthInfo(vo.getApiId(),
                roleId,
                role,
                weightId,
                weight);

        String key = Constant.MEMBER_REDIS_PREFIX + "api_auth_infos";
        //删除权限缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 获取合法的当前请求路径
     * @return 合法的当前请求路径
     */
    private String getValidateRequestUrl(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String requestURI = request.getRequestURI();

        int index = requestURI.length() - 1;
        StringBuilder stringBuilder = new StringBuilder(requestURI);
        //去除请求路径最后的 / 字符
        while(index >= 0 && stringBuilder.charAt(index) == '/'){
            stringBuilder.deleteCharAt(index--);
        }

        return stringBuilder.toString();
    }
}
