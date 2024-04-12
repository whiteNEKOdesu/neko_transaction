package neko.transaction.product.mapper;

import neko.transaction.product.entity.ReturnApplyImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 退货申请图片表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
@Mapper
public interface ReturnApplyImageMapper extends BaseMapper<ReturnApplyImage> {
    /**
     * 批量添加退回申请图片
     * @param returnApplyImages 退货申请图片信息
     */
    void insertBatch(List<ReturnApplyImage> returnApplyImages);
}
