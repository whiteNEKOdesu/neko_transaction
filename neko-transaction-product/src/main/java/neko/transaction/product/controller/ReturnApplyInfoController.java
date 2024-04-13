package neko.transaction.product.controller;

import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.ReturnApplyInfoService;
import neko.transaction.product.vo.NewReturnApplyVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
