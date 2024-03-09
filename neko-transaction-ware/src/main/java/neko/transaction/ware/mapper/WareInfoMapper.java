package neko.transaction.ware.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.ware.entity.WareInfo;
import neko.transaction.ware.vo.LockStockVo;
import neko.transaction.ware.vo.WareInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 库存信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Mapper
public interface WareInfoMapper extends BaseMapper<WareInfo> {
    /**
     * 根据商品id获取库存信息
     * @param productId 商品id
     * @return 库存信息
     */
    WareInfoVo wareInfoById(String productId);

    /**
     * 根据商品id修改库存剩余数量
     * @param productId 商品id
     * @param offset 库存偏移量
     */
    int updateStockByProductId(String productId, int offset);

    /**
     * 锁定指定库存id的库存
     * @param wareId 库存id
     * @param lockNumber 锁定数量
     */
    int lockStock(Long wareId,
                  Integer lockNumber);

    /**
     * 批量锁定库存
     * @param lockProductInfos 锁定的库存信息
     */
    int lockStocks(List<LockStockVo.LockProductInfo> lockProductInfos);

    /**
     * 解锁指定库存id，库存锁定记录id的库存
     * @param wareId 库存id
     * @param stockLockLogId 库存锁定记录id
     */
    int unlockStock(Long wareId,
                    Long stockLockLogId);

    /**
     * 订单支付确认后，减少对应的库存数量字段，以及锁定的数量字段
     * @param wareId 库存id
     * @param lockNumber 减少的库存数量
     */
    void decreaseStock(Long wareId,
                       Integer lockNumber);
}
