package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.product.entity.ProductComment;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.mapper.ProductCommentMapper;
import neko.transaction.product.service.ProductCommentService;
import neko.transaction.product.to.MemberInfoTo;
import neko.transaction.product.vo.NewProductCommentVo;
import neko.transaction.product.vo.ProductScoreVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    @Resource
    private MemberInfoFeignService memberInfoFeignService;

    /**
     * 添加商品评论
     * @param vo 添加商品评论vo
     */
    @Override
    public void newProductComment(NewProductCommentVo vo) {
        String uid = StpUtil.getLoginId().toString();
        ProductComment productComment = new ProductComment();
        BeanUtil.copyProperties(vo, productComment);
        productComment.setUid(uid);

        //非匿名评论
        if(!vo.getIsNick()){
            //远程调用用户微服务获取用户信息
            ResultObject<MemberInfoTo> r = memberInfoFeignService.userSelfInfo(StpUtil.getTokenValue());
            if(!r.getResponseCode().equals(200)){
                throw new MemberServiceException("member微服务远程调用异常");
            }

            productComment.setRealName(r.getResult().getRealName());
        }

        this.baseMapper.insert(productComment);
    }

    /**
     * 分页查询商品评论信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductComment> productCommentPageQuery(QueryVo vo) {
        Object objectId = vo.getObjectId();
        if(objectId == null){
            throw new IllegalArgumentException("缺少商品id");
        }

        String productId = objectId.toString();
        Page<ProductComment> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<ProductComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductComment::getProductId, productId)
                .eq(ProductComment::getIsDelete, false)
                .orderByDesc(ProductComment::getCommentId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 根据商品id获取商品评分信息
     * @param productId 商品id
     * @return 商品评分信息
     */
    @Override
    public ProductScoreVo getProductScoreByProductId(String productId) {
        return this.baseMapper.getProductScoreByProductId(productId);
    }
}
