package neko.transaction.product.mapper;

import neko.transaction.product.entity.ProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.ProductInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Mapper
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    /**
     * 分页查询学生自身的商品信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @param uid 学生id
     * @param status 商品状态
     * @return 查询结果
     */
    List<ProductInfoVo> userSelfPageQuery(Integer limited,
                                          Integer start,
                                          String queryWords,
                                          String uid,
                                          Byte status);

    /**
     * 分页查询学生自身的商品信息的结果总数
     * @param queryWords 查询条件
     * @param uid 学生id
     * @param status 商品状态
     * @return 查询结果的总数
     */
    int userSelfPageQueryNumber(String queryWords,
                                String uid,
                                Byte status);
}
