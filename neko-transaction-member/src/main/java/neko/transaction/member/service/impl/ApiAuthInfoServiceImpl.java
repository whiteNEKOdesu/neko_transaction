package neko.transaction.member.service.impl;

import neko.transaction.member.entity.ApiAuthInfo;
import neko.transaction.member.mapper.ApiAuthInfoMapper;
import neko.transaction.member.service.ApiAuthInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ApiAuthInfoServiceImpl extends ServiceImpl<ApiAuthInfoMapper, ApiAuthInfo> implements ApiAuthInfoService {
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 将 api 信息同步到数据库
     */
    @Override
    public void synchronizeApiToDB() {
        //需要新同步的 api 信息
        List<ApiAuthInfo> newVersions = new ArrayList<>();

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                .getHandlerMethods();
        map.forEach((key, value) -> {
            ApiAuthInfo apiAuthInfo = new ApiAuthInfo();
            PathPatternsRequestCondition pathPatternsCondition = key.getPathPatternsCondition();
            if(pathPatternsCondition == null){
                return;
            }

            apiAuthInfo.setPath(pathPatternsCondition.getFirstPattern().toString())
                    .setRequestMethod(key.getMethodsCondition().toString())
                    .setHandlerMethod(value.getMethod().getName());

            newVersions.add(apiAuthInfo);
        });

        //需要新同步的 api 信息的路径
        List<String> paths = newVersions.stream().map(ApiAuthInfo::getPath)
                .collect(Collectors.toList());
    }
}
