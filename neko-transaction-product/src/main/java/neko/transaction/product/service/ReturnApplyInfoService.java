package neko.transaction.product.service;

import neko.transaction.product.entity.ReturnApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewReturnApplyVo;

/**
 * <p>
 * 退货申请表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-11
 */
public interface ReturnApplyInfoService extends IService<ReturnApplyInfo> {
    /**
     * 添加退货申请
     * @param vo 添加退货申请vo
     */
    void newReturnApplyInfo(NewReturnApplyVo vo);
}
