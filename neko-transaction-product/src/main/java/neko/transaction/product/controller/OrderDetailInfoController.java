package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.product.service.OrderDetailInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author NEKO
 * @since 2024-01-27
 */
@RestController
@RequestMapping("order_detail_info")
public class OrderDetailInfoController {
    @Resource
    private OrderDetailInfoService orderDetailInfoService;

    /**
     * 学生确认收货
     * @param orderDetailId 订单详情id
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("confirm_received")
    public ResultObject<Object> confirmReceived(@RequestParam String orderDetailId){
        orderDetailInfoService.confirmReceived(orderDetailId);

        return ResultObject.ok();
    }
}
