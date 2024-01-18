package neko.transaction.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductApplyInfo;
import neko.transaction.product.vo.ProductApplyInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品上架申请信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Mapper
public interface ProductApplyInfoMapper extends BaseMapper<ProductApplyInfo> {
    /**
     * 分页查询未处理的商品上架请求
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @return 查询结果
     */
    List<ProductApplyInfoVo> unhandledApplyPageQuery(Integer limited,
                                                     Integer start,
                                                     String queryWords);

    /**
     * 分页查询未处理的商品上架请求的结果总页数
     * @param queryWords 查询条件
     * @return 查询结果的总结果数
     */
    int unhandledApplyPageQueryNumber(String queryWords);
}
