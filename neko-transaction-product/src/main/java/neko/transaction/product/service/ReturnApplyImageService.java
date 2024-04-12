package neko.transaction.product.service;

import neko.transaction.product.entity.ReturnApplyImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 退货申请图片表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
public interface ReturnApplyImageService extends IService<ReturnApplyImage> {
    /**
     * 批量添加退回申请图片
     * @param returnApplyImages 退货申请图片信息
     */
    void insertBatch(List<ReturnApplyImage> returnApplyImages);
}
