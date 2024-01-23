package neko.transaction.ware.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.ware.entity.WareInfo;
import neko.transaction.ware.mapper.WareInfoMapper;
import neko.transaction.ware.service.WareInfoService;
import neko.transaction.ware.vo.WareInfoVo;
import org.springframework.stereotype.Service;

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
}
