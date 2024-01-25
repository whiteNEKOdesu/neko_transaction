package neko.transaction.ware.service;

import neko.transaction.ware.entity.WareInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.ware.vo.WareInfoVo;

/**
 * <p>
 * 库存信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
public interface WareInfoService extends IService<WareInfo> {
    /**
     * 根据商品id获取库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    WareInfoVo wareInfoById(String productId);

    /**
     * 添加库存信息
     * @param productId 商品id
     * @param stock 库存数量
     */
    void newWareInfo(String productId, Integer stock);

    /**
     * 修改库存数量
     * @param productId 商品id
     * @param offset 库存偏移量
     */
    void updateStock(String productId, int offset);
}