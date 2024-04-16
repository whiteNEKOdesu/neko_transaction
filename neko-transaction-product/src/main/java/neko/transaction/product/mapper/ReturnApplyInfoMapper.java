package neko.transaction.product.mapper;

import neko.transaction.product.entity.ReturnApplyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.ReturnApplyInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 分页查询申请退货信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @param status 退款审核状态
     * @return 查询结果
     */
    List<ReturnApplyInfoVo> returnApplyInfoPageQuery(Integer limited,
                                                   Integer start,
                                                   String queryWords,
                                                   Byte status);

    /**
     * 分页查询申请退货信息的结果总数量
     * @param queryWords 查询条件
     * @param status 退款审核状态
     * @return 查询结果的结果总数量
     */
    int returnApplyInfoPageQueryNumber(String queryWords,
                                       Byte status);
}
