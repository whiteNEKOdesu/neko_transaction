package neko.transaction.product.service.impl;

import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.mapper.CategoryInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
