package neko.transaction.product.mapper;

import neko.transaction.product.entity.ProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.product.vo.ProductDetailInfoVo;
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

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @param uid 学生学号
     * @return 查询结果
     */
    ProductInfoVo getUserSelfProductInfoById(String productId, String uid);

    /**
     * 下架商品，更新商品状态为下架状态
     * @param productId 商品id
     * @param uid 学生学号
     */
    void downProduct(String productId, String uid);

    /**
     * 获取上架的商品详情信息
     * @param productId 商品id
     * @return 商品详情信息
     */
    ProductDetailInfoVo getUpProductDetailInfo(String productId);

    /**
     * 根据商品id集合获取商品详情信息
     * @param productIds 商品id集合
     * @return 商品id集合对应的商品详情信息
     */
    List<ProductDetailInfoVo> getProductDetailInfoByIds(List<String> productIds);

    /**
     * 添加销量
     * @param productId 商品id
     * @param increase 要添加的数量
     */
    void increaseSaleNumber(String productId, Integer increase);
}
