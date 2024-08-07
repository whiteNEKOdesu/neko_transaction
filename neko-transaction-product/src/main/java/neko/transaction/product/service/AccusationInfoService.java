package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.AccusationInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.AccusationInfoVo;
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

    /**
     * 分页查询举报信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<AccusationInfoVo> unhandledAccusationInfoPageQuery(QueryVo vo);

    /**
     * 驳回举报
     * @param accuseId 举报id
     */
    void rejectAccusation(Long accuseId);

    /**
     * 通过举报
     * @param accuseId 举报id
     */
    void passAccusation(Long accuseId);

    /**
     * 获取封禁原因
     * @param productId 商品id
     * @return 封禁原因
     */
    String getBanReason(String productId);
}
