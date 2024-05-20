package neko.transaction.member.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.member.entity.ApiAuthInfo;
import neko.transaction.member.service.ApiAuthInfoService;
import neko.transaction.member.vo.UpdateApiAuthInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * api鉴权信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-05-19
 */
@RestController
@RequestMapping("api_auth_info")
public class ApiAuthInfoController {
    @Resource
    private ApiAuthInfoService apiAuthInfoService;

    /**
     * 将 api 信息同步到数据库
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("synchronize_api_info_to_db")
    public ResultObject<Object> synchronizeApiInfoToDB(){
        apiAuthInfoService.synchronizeApiToDB();

        return ResultObject.ok();
    }

    /**
     * 分页查询 api 鉴权信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("page_query")
    public ResultObject<Page<ApiAuthInfo>> pageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(apiAuthInfoService.pageQuery(vo));
    }

    /**
     * 修改 api 鉴权信息
     * @param vo 修改 api 鉴权信息vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ROOT)
    @PostMapping("update_api_auth_info")
    public ResultObject<Object> updateApiAuthInfo(@Validated @RequestBody UpdateApiAuthInfoVo vo){
        apiAuthInfoService.updateApiAuthInfo(vo);

        return ResultObject.ok();
    }
}
