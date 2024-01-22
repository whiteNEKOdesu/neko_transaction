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
}
