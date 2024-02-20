package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.MemberServiceException;
import neko.transaction.product.entity.ProductComment;
import neko.transaction.product.feign.member.MemberInfoFeignService;
import neko.transaction.product.mapper.ProductCommentMapper;
import neko.transaction.product.service.ProductCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.to.MemberInfoTo;
import neko.transaction.product.vo.NewProductCommentVo;
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
}
