package neko.transaction.product.service;

import neko.transaction.product.entity.AccusationType;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewAccusationTypeVo;

/**
 * <p>
 * 举报类型信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
public interface AccusationTypeService extends IService<AccusationType> {
    /**
     * 添加举报类型信息
     * @param vo 添加举报类型信息vo
     */
    void newAccusationType(NewAccusationTypeVo vo);
}
