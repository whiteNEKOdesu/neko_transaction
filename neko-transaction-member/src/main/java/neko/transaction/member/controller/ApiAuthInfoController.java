package neko.transaction.member.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.member.service.ApiAuthInfoService;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("api_auth_nfo")
public class ApiAuthInfoController {
    @Resource
    private ApiAuthInfoService apiAuthInfoService;

    /**
     * 将 api 信息同步到数据库
     * @return 响应结果
     */
    @PostMapping("synchronize_api_info_to_db")
    public ResultObject<Object> synchronizeApiInfoToDB(){
        apiAuthInfoService.synchronizeApiToDB();

        return ResultObject.ok();
    }
}
