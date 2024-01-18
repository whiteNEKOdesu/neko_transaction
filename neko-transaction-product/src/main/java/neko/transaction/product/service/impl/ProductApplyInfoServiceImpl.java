package neko.transaction.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.ThirdPartyServiceException;
import neko.transaction.product.entity.CategoryInfo;
import neko.transaction.product.entity.ProductApplyInfo;
import neko.transaction.product.feign.thirdparty.OSSFeignService;
import neko.transaction.product.mapper.ProductApplyInfoMapper;
import neko.transaction.product.service.CategoryInfoService;
import neko.transaction.product.service.ProductApplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewProductApplyInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 商品上架申请信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
@Service
public class ProductApplyInfoServiceImpl extends ServiceImpl<ProductApplyInfoMapper, ProductApplyInfo> implements ProductApplyInfoService {
    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private OSSFeignService ossFeignService;

    /**
     * 添加商品上架申请信息
     * @param vo 商品上架申请信息的vo
     */
    @Override
    public void newProductApplyInfo(NewProductApplyInfoVo vo) {
        CategoryInfo categoryInfo = categoryInfoService.getById(vo.getCategoryId());
        if(categoryInfo == null || !categoryInfo.getLevel().equals(1)){
            throw new IllegalArgumentException("分类id不存在或不为叶节点");
        }

        //上传图片到oss
        ResultObject<String> r = ossFeignService.uploadImage(vo.getApplyImage());
        if(!r.getResponseCode().equals(200)){
            throw new ThirdPartyServiceException("thirdparty微服务远程调用异常");
        }


        ProductApplyInfo productApplyInfo = new ProductApplyInfo();
        BeanUtil.copyProperties(vo, productApplyInfo);
        //设置上传的图片url
        productApplyInfo.setApplyImage(r.getResult())
                //设置价格精度
                .setPrice(productApplyInfo.getPrice()
                        .setScale(2, BigDecimal.ROUND_DOWN));

        //添加商品上架申请信息
        this.baseMapper.insert(productApplyInfo);
    }
}
