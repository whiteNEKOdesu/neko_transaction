package neko.transaction.product.mapper;

import neko.transaction.product.entity.ReturnApplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.ReturnApplyInfoVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 退货申请表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Mapper
public interface ReturnApplyInfoMapper extends BaseMapper<ReturnApplyInfo> {
    /**
     * 根据 订单详情id 获取申请退货信息
     * @param orderDetailId 订单详情id
     * @return 申请退货信息
     */
    ReturnApplyInfoVo getReturnApplyInfoByOrderDetailId(String orderDetailId);
}
