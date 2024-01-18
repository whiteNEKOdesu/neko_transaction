package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.vo.NewCategoryInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品分类信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@RestController
@RequestMapping("category_info")
public class CategoryInfoController {
    @Resource
    private CategoryInfoService categoryInfoService;

    /**
     * 获取层级商品分类信息
     * @return 层级商品分类信息
     */
    @GetMapping("level_category_info")
    public ResultObject<List<CategoryInfo>> levelCategoryInfo(){
        return ResultObject.ok(categoryInfoService.getLevelCategory());
    }

    /**
     * 管理员新增影视分类信息
     * @param vo 新增影视分类信息的vo
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PutMapping("new_category_info")
    public ResultObject<Object> newCategoryInfo(@Validated @RequestBody NewCategoryInfoVo vo){
        categoryInfoService.newCategoryInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 管理员删除叶节点影视分类信息
     * @param categoryId 分类id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @DeleteMapping("delete_leaf_category_info")
    public ResultObject<Object> deleteLeafCategoryInfo(@RequestParam Integer categoryId) {
        categoryInfoService.deleteLeafCategoryInfo(categoryId);

        return ResultObject.ok();
    }
}
