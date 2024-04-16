package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ReturnApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewReturnApplyVo;
import neko.transaction.product.vo.ReturnApplyInfoVo;

/**
 * <p>
 * 退货申请表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
public interface ReturnApplyInfoService extends IService<ReturnApplyInfo> {
    /**
     * 添加退货申请
     * @param vo 添加退货申请vo
     */
    void newReturnApplyInfo(NewReturnApplyVo vo);

    /**
     * 根据 订单详情id 获取申请退货信息
     * @param orderDetailId 订单详情id
     * @return 申请退货信息
     */
    ReturnApplyInfoVo getReturnApplyInfoByOrderDetailId(String orderDetailId);

    /**
     * 分页查询申请退货信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ReturnApplyInfoVo> returnApplyInfoPageQuery(QueryVo vo);
}
