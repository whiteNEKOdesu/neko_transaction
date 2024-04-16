package neko.transaction.product.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.entity.RoleType;
import neko.transaction.product.service.OrderDetailInfoService;
import neko.transaction.product.vo.CensorReturnApplyVo;
import neko.transaction.product.vo.NewReturnApplyVo;
import neko.transaction.product.vo.OrderDetailInfoVo;
import neko.transaction.product.vo.OrderDetailStatusAggVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 分页查询卖家自身的订单详情信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @SaCheckLogin
    @PostMapping("seller_self_page_query")
    public ResultObject<Page<OrderDetailInfoVo>> sellerSelfPageQuery(@Validated @RequestBody QueryVo vo){
        return ResultObject.ok(orderDetailInfoService.sellerSelfPageQuery(vo));
    }

    /**
     * 获取订单详情按照状态聚合信息
     * @return 订单详情按照状态聚合信息
     */
    @SaCheckRole(RoleType.ADMIN)
    @GetMapping("status_agg")
    public ResultObject<List<OrderDetailStatusAggVo>> statusAgg(){
        return ResultObject.ok(orderDetailInfoService.statusAggCount());
    }

    /**
     * 添加退货申请
     * @param vo 添加退货申请vo
     * @return 响应结果
     */
    @PostMapping("new_return_apply")
    public ResultObject<Object> newReturnApply(@Validated NewReturnApplyVo vo){
        orderDetailInfoService.newReturnApplyInfo(vo);

        return ResultObject.ok();
    }

    /**
     * 卖家审核退货申请
     * @param vo 提交审核退货申请vo
     * @return 响应结果
     */
    @SaCheckLogin
    @PostMapping("seller_censor_return_appply")
    public ResultObject<Object> sellerCensorReturnApply(@Validated @RequestBody CensorReturnApplyVo vo){
        orderDetailInfoService.sellerCensorReturnApply(vo);

        return ResultObject.ok();
    }
}
