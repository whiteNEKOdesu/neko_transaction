package neko.transaction.product.service.impl;

import neko.transaction.product.entity.ReturnApplyImage;
import neko.transaction.product.mapper.ReturnApplyImageMapper;
import neko.transaction.product.service.ReturnApplyImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 退货申请图片表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Service
public class ReturnApplyImageServiceImpl extends ServiceImpl<ReturnApplyImageMapper, ReturnApplyImage> implements ReturnApplyImageService {

    /**
     * 批量添加退回申请图片
     * @param returnApplyImages 退货申请图片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<ReturnApplyImage> returnApplyImages) {
        this.baseMapper.insertBatch(returnApplyImages);
    }
}
