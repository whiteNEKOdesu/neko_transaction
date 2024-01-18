package neko.transaction.product.service;

import neko.transaction.product.entity.ProductApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewProductApplyInfoVo;

/**
 * <p>
 * 商品上架申请信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-17
 */
public interface ProductApplyInfoService extends IService<ProductApplyInfo> {
    /**
     * 添加商品上架申请信息
     * @param vo 商品上架申请信息的vo
     */
    void newProductApplyInfo(NewProductApplyInfoVo vo);
}
