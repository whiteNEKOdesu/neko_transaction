package neko.transaction.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import neko.transaction.commonbase.utils.entity.Constant;
import neko.transaction.commonbase.utils.exception.NoSuchResultException;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.mapper.CategoryInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewCategoryInfoVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取层级商品分类信息
     * @return 层级商品分类信息
     */
    @Override
    public List<CategoryInfo> getLevelCategory() {
        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        String cache = stringRedisTemplate.opsForValue().get(key);

        //缓存有数据
        if(cache != null){
            return JSONUtil.toList(JSONUtil.parseArray(cache), CategoryInfo.class);
        }

        List<CategoryInfo> categoryInfos = this.list();
        //找到父分类
        List<CategoryInfo> parentCategoryInfos = categoryInfos.stream().filter(Objects::nonNull)
                .filter(categoryInfo -> categoryInfo.getLevel().equals(0))
                .collect(Collectors.toList());

        //递归设置子分类信息
        List<CategoryInfo> result = parentCategoryInfos.stream().peek(categoryInfo -> categoryInfo.setChild(getLevelCategory(categoryInfo, categoryInfos)))
                .collect(Collectors.toList());
        //缓存无数据，查询存入缓存
        stringRedisTemplate.opsForValue().setIfAbsent(key,
                JSONUtil.toJsonStr(result),
                1000 * 60 * 60 * 5,
                TimeUnit.MILLISECONDS);

        return result;
    }

    /**
     * 新增商品分类信息
     * @param vo 新增商品分类信息的vo
     */
    @Override
    public void newCategoryInfo(NewCategoryInfoVo vo) {
        if(vo.getParentId() != null && this.baseMapper.selectById(vo.getParentId()) == null){
            throw new NoSuchResultException("无此分类id");
        }

        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtil.copyProperties(vo, categoryInfo);
        LocalDateTime now = LocalDateTime.now();
        categoryInfo.setCreateTime(now)
                .setUpdateTime(now);

        this.baseMapper.insert(categoryInfo);

        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        //删除缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除叶节点商品分类信息
     * @param categoryId 分类id
     */
    @Override
    public void deleteLeafCategoryInfo(Integer categoryId) {
        this.baseMapper.deleteLeafCategoryInfo(categoryId);

        String key = Constant.PRODUCT_REDIS_PREFIX + "level_category";
        //删除缓存
        stringRedisTemplate.delete(key);
    }

    /**
     * 递归设置子分类信息
     * @param root 父分类
     * @param all 所有分类信息
     * @return 树形分类结果
     */
    private List<CategoryInfo> getLevelCategory(CategoryInfo root, List<CategoryInfo> all){
        return all.stream().filter(categoryInfo -> root.getCategoryId().equals(categoryInfo.getParentId()))
                .peek(categoryInfo -> {
                    List<CategoryInfo> todoChild = getLevelCategory(categoryInfo, all);
                    categoryInfo.setChild(!todoChild.isEmpty() || !categoryInfo.getLevel().equals(1) ? todoChild : null);
                }).collect(Collectors.toList());
    }
}
