package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
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

    /**
     * 分页查询商品评论信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ProductComment> productCommentPageQuery(QueryVo vo);
}
