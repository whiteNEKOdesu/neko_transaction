package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.AccusationInfoService;
import neko.transaction.product.vo.NewAccusationInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 举报信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@RestController
@RequestMapping("accusation_info")
public class AccusationInfoController {
    @Resource
    private AccusationInfoService accusationInfoService;

    /**
     * 添加举报信息
     * @param vo 添加举报信息vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PutMapping("new_accusation_info")
    public ResultObject<Object> newAccusationInfo(@Validated @RequestBody NewAccusationInfoVo vo){
        accusationInfoService.newAccusationInfo(vo);

        return ResultObject.ok();
    }
}
