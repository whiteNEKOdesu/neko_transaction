package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.service.ProductApplyInfoService;
import neko.transaction.product.vo.NewProductApplyInfoVo;
import neko.transaction.product.vo.ProductApplyInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 商品上架申请信息表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@RestController
@RequestMapping("product_apply_info")
public class ProductApplyInfoController {
    @Resource
    private ProductApplyInfoService productApplyInfoService;

    /**
     * 添加商品上架申请信息
     * @param vo 商品上架申请信息的vo
     */
    @SaCheckLogin
    @PostMapping("new_apply_info")
    public ResultObject<Object> newApplyInfo(@Validated NewProductApplyInfoVo vo){
        productApplyInfoService.newProductApplyInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 分页查询未处理的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("unhandled_apply_page_query")
    public ResultObject<Page<ProductApplyInfoVo>> unhandledApplyPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(productApplyInfoService.unhandledApplyPageQuery(vo));
    }

    /**
     * 管理员通过商品上架申请
     * @param productApplyId 申请id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("pass_apply")
    public ResultObject<Object> applyObject(@RequestParam Long productApplyId){
        productApplyInfoService.passApply(productApplyId);

        return ResultObject.ok();
    }

    /**
     * 管理员拒绝商品上架申请
     * @param productApplyId 申请id
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("reject_apply")
    public ResultObject<Object> rejectApply(@RequestParam Long productApplyId){
        productApplyInfoService.rejectApply(productApplyId);

        return ResultObject.ok();
    }

    /**
     * 分页查询学生自身的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckLogin
    @PostMapping("user_self_page_query")
    public ResultObject<Page<ProductApplyInfoVo>> userSelfPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(productApplyInfoService.userSelfApplyPageQuery(vo));
    }
}
