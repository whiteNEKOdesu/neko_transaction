package neko.transaction.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductApplyInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.product.vo.NewProductApplyInfoVo;
import neko.transaction.product.vo.ProductApplyInfoVo;

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

    /**
     * 分页查询未处理的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ProductApplyInfoVo> unhandledApplyPageQuery(QueryVo vo);

    /**
     * 通过商品上架申请
     * @param productApplyId 申请id
     */
    void passApply(Long productApplyId);

    /**
     * 拒绝商品上架申请
     * @param productApplyId 申请id
     */
    void rejectApply(Long productApplyId);

    /**
     * 分页查询学生自身的商品上架请求
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ProductApplyInfoVo> userSelfApplyPageQuery(QueryVo vo);
}
