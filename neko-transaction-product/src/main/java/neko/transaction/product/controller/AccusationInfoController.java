package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.service.AccusationInfoService;
import neko.transaction.product.vo.AccusationInfoVo;
import neko.transaction.product.vo.NewAccusationInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 分页查询举报信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("unhandled_accusation_info_page_query")
    public ResultObject<Page<AccusationInfoVo>> unhandledAccusationInfoPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(accusationInfoService.unhandledAccusationInfoPageQuery(vo));
    }

    /**
     * 驳回举报
     * @param accuseId 举报id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("reject_accusation")
    public ResultObject<Object> rejectAccusation(@RequestParam Long accuseId){
        accusationInfoService.rejectAccusation(accuseId);

        return ResultObject.ok();
    }

    /**
     * 通过举报
     * @param accuseId 举报id
     * @return 响应结果
     */
    @SaCheckRole(RoleType.ADMIN)
    @PostMapping("pass_accusation")
    public ResultObject<Object> passAccusation(@RequestParam Long accuseId){
        accusationInfoService.passAccusation(accuseId);

        return ResultObject.ok();
    }

    /**
     * 获取封禁原因
     * @param productId 商品id
     * @return 封禁原因
     */
    @GetMapping("ban_reason_by_product_id")
    public ResultObject<String> banReasonByProductId(@RequestParam String productId){
        return ResultObject.ok(accusationInfoService.getBanReason(productId));
    }
}
