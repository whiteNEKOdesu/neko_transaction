package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.entity.AccusationType;
import neko.transaction.product.service.AccusationTypeService;
import neko.transaction.product.vo.NewAccusationTypeVo;
import neko.transaction.product.vo.UpdateAccusationTypeVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 分页查询举报类型信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("accusation_type_page_query")
    public ResultObject<Page<AccusationType>> accusationTypePageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(accusationTypeService.accusationTypePageQuery(vo));
    }

    /**
     * 修改举报类型信息
     * @param vo 修改举报类型信息vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("update_accusation_type")
    public ResultObject<Object> updateAccusationType(@Validated @RequestBody UpdateAccusationTypeVo vo){
        accusationTypeService.updateAccusationType(vo);

        return ResultObject.ok();
    }

    /**
     * 删除举报类型
     * @param accuseTypeId 举报类型id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @DeleteMapping("delete_accusation_type")
    public ResultObject<Object> deleteAccusationType(@RequestParam Integer accuseTypeId){
        accusationTypeService.deleteAccusationType(accuseTypeId);

        return ResultObject.ok();
    }
}
