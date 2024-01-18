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
    /**
     * 获取层级商品分类信息
     * @return 层级商品分类信息
     */
    List<CategoryInfo> getLevelCategory();

    /**
     * 新增商品分类信息
     * @param vo 新增商品分类信息的vo
     */
    void newCategoryInfo(NewCategoryInfoVo vo);

    /**
     * 删除叶节点商品分类信息
     * @param categoryId 分类id
     */
    void deleteLeafCategoryInfo(Integer categoryId);

    /**
     * 根据 categoryId 获取全分类名
     * @param categoryId 分类id
     * @return 全分类名
     */
    String getFullCategoryName(Integer categoryId);
}
