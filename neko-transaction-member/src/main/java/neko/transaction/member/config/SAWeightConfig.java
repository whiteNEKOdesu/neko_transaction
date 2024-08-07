package neko.transaction.member.config;

import cn.dev33.satoken.stp.StpInterface;
import neko.transaction.member.service.WeightRoleRelationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限设置配置
 */
@Component
public class SAWeightConfig implements StpInterface {
    @Resource
    private WeightRoleRelationService weightRoleRelationService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return weightRoleRelationService.getWeightTypesByUid(o.toString());
    }

    @Override
    // TODO
    public List<String> getRoleList(Object o, String s) {
        return weightRoleRelationService.getRoleTypesByUid(o.toString());
    }
}
