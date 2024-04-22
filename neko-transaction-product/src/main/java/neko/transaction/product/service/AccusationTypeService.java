package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.AccusationType;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewAccusationTypeVo;
import neko.transaction.product.vo.UpdateAccusationTypeVo;

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

    /**
     * 分页查询举报类型信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<AccusationType> accusationTypePageQuery(QueryVo vo);

    /**
     * 修改举报类型信息
     * @param vo 修改举报类型信息vo
     */
    void updateAccusationType(UpdateAccusationTypeVo vo);

    /**
     * 删除举报类型
     * @param accuseTypeId 举报类型id
     */
    void deleteAccusationType(Integer accuseTypeId);
}
