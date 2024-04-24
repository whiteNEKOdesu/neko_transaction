package neko.transaction.product.mapper;

import neko.transaction.product.entity.AccusationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.AccusationInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 举报信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Mapper
public interface AccusationInfoMapper extends BaseMapper<AccusationInfo> {
    /**
     * 分页查询举报信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @param accuseTypeId 举报类型id
     * @return 查询结果
     */
    List<AccusationInfoVo> unhandledAccusationInfoPageQuery(Integer limited,
                                                            Integer start,
                                                            String queryWords,
                                                            Integer accuseTypeId);

    /**
     * 分页查询举报信息的结果总数量
     * @param queryWords 查询条件
     * @param accuseTypeId 举报类型id
     * @return 查询结果的结果总数量
     */
    int unhandledAccusationInfoPageQueryNumber(String queryWords,
                                               Integer accuseTypeId);

    /**
     * 获取封禁原因
     * @param productId 商品id
     * @return 封禁原因
     */
    String getBanReason(String productId);
}
