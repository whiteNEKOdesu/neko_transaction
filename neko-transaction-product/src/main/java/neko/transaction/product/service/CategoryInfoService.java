package neko.transaction.product.service;

import neko.transaction.product.entity.CategoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewCategoryInfoVo;

import java.util.List;

/**
 * <p>
 * 商品分类信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
public interface CategoryInfoService extends IService<CategoryInfo> {
    List<CategoryInfo> getLevelCategory();

    void newCategoryInfo(NewCategoryInfoVo vo);

    void deleteLeafCategoryInfo(Integer categoryId);
}
