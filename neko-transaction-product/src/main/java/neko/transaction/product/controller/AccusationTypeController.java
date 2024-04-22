package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.service.AccusationTypeService;
import neko.transaction.product.vo.NewAccusationTypeVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 举报类型信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@RestController
@RequestMapping("accusation_type")
public class AccusationTypeController {
    @Resource
    private AccusationTypeService accusationTypeService;

    /**
     * 添加举报类型信息
     * @param vo 添加举报类型信息vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PutMapping("new_accusation_type")
    public ResultObject<Object> newAccusationType(@Validated @RequestBody NewAccusationTypeVo vo){
        accusationTypeService.newAccusationType(vo);

        return ResultObject.ok();
    }
}
