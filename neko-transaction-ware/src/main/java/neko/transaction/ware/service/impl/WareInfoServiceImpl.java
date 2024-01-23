package neko.transaction.ware.service.impl;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.ResultObject;
import neko.transaction.commonbase.utils.exception.ProductServiceException;
import neko.transaction.commonbase.utils.exception.StockNotEnoughException;
import neko.transaction.ware.entity.WareInfo;
import neko.transaction.ware.feign.product.ProductInfoFeignService;
import neko.transaction.ware.mapper.WareInfoMapper;
import neko.transaction.ware.service.WareInfoService;
import neko.transaction.ware.to.ProductInfoTo;
import neko.transaction.ware.vo.WareInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 库存信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Service
public class WareInfoServiceImpl extends ServiceImpl<WareInfoMapper, WareInfo> implements WareInfoService {
    @Resource
    private ProductInfoFeignService productInfoFeignService;

    /**
     * 根据商品id获取库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    @Override
    public WareInfoVo wareInfoById(String productId) {
        return this.baseMapper.wareInfoById(productId);
    }

    /**
     * 添加库存信息
     * @param productId 商品id
     * @param stock 库存数量
     */
    @Override
    public void newWareInfo(String productId, Integer stock) {
        WareInfo wareInfo = new WareInfo();
        wareInfo.setProductId(productId)
                .setStock(stock);

        this.baseMapper.insert(wareInfo);
    }

    /**
     * 修改库存数量
     * @param productId 商品id
     * @param offset 库存偏移量
     */
    @Override
    public void updateStock(String productId, int offset) {
        String token = StpUtil.getTokenValue();
        ResultObject<ProductInfoTo> r = productInfoFeignService.userSelfProductById(productId, token);
        if(!r.getResponseCode().equals(200)){
            throw new ProductServiceException("product微服务远程调用异常");
        }
        if(r.getResult() == null){
            throw new NotPermissionException("商品不属于此用户");
        }

        //修改库存数量
        if(this.baseMapper.updateStockByProductId(productId, offset) != 1){
            throw new StockNotEnoughException("库存修改后小于0");
        }
    }
}
