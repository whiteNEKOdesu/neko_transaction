package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.ProductInfo;
import neko.transaction.product.mapper.ProductInfoMapper;
import neko.transaction.product.service.ProductInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.ProductApplyInfoVo;
import neko.transaction.product.vo.ProductInfoVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-18
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

    /**
     * 分页查询学生自身的商品信息
     * @param vo 查询vo
     * @return 查询结果
     */
    @Override
    public Page<ProductInfoVo> userSelfPageQuery(QueryVo vo) {
        Page<ProductInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        String uid = (String) StpUtil.getLoginId();
        Object objectId = vo.getObjectId();
        Byte status = objectId != null ? Byte.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.userSelfPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                uid,
                status));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.userSelfPageQueryNumber(vo.getQueryWords(),
                uid,
                status));

        return page;
    }

    /**
     * 根据商品id查询用户自己的商品信息
     * @param productId 商品id
     * @return 查询结果
     */
    @Override
    public ProductInfoVo getUserSelfProductInfoById(String productId) {
        return this.baseMapper.getUserSelfProductInfoById(productId, (String) StpUtil.getLoginId());
    }
}
