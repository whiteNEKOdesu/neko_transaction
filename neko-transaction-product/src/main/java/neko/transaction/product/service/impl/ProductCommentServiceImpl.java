package neko.transaction.product.service.impl;

import neko.transaction.product.entity.ProductComment;
import neko.transaction.product.mapper.ProductCommentMapper;
import neko.transaction.product.service.ProductCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评论表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-02-20
 */
@Service
public class ProductCommentServiceImpl extends ServiceImpl<ProductCommentMapper, ProductComment> implements ProductCommentService {

}
