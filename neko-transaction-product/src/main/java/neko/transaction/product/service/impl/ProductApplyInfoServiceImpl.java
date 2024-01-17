package neko.transaction.product.service.impl;

import neko.transaction.product.entity.ProductApplyInfo;
import neko.transaction.product.mapper.ProductApplyInfoMapper;
import neko.transaction.product.service.ProductApplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
