package neko.transaction.product.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.ReturnApplyInfoService;
import neko.transaction.product.vo.NewReturnApplyVo;
import neko.transaction.product.vo.ReturnApplyInfoVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 退货申请表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@RestController
@RequestMapping("return_apply_info")
public class ReturnApplyInfoController {
    @Resource
    private ReturnApplyInfoService returnApplyInfoService;

    /**
     * 根据 订单详情id 获取申请退货信息
     * @param orderDetailId 订单详情id
     * @return 申请退货信息
     */
    @GetMapping("return_apply_info_by_id")
    public ResultObject<ReturnApplyInfoVo> returnApplyInfoById(@RequestParam String orderDetailId){
        return ResultObject.ok(returnApplyInfoService.getReturnApplyInfoByOrderDetailId(orderDetailId));
    }
}
