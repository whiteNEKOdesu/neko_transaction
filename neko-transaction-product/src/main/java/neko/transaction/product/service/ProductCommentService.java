package neko.transaction.product.service;

import neko.transaction.product.entity.ProductComment;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewProductCommentVo;

/**
 * <p>
 * 商品评论表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-02-20
 */
public interface ProductCommentService extends IService<ProductComment> {
    /**
     * 添加商品评论
     * @param vo 添加商品评论vo
     */
    void newProductComment(NewProductCommentVo vo);
}
