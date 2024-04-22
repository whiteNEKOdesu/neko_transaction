package neko.transaction.product.service;

import neko.transaction.product.entity.AccusationInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewAccusationInfoVo;

/**
 * <p>
 * 举报信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
public interface AccusationInfoService extends IService<AccusationInfo> {
    /**
     * 添加举报信息
     * @param vo 添加举报信息vo
     */
    void newAccusationInfo(NewAccusationInfoVo vo);
}
