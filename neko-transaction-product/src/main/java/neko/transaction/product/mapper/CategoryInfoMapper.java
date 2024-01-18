package neko.transaction.product.mapper;

import neko.transaction.product.entity.CategoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品分类信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Mapper
public interface CategoryInfoMapper extends BaseMapper<CategoryInfo> {
    /**
     * 根据 categoryId 删除叶节点
     * @param categoryId 分类id
     */
    void deleteLeafCategoryInfo(Integer categoryId);
}
