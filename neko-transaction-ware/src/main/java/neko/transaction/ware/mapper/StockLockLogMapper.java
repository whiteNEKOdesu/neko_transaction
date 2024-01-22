package neko.transaction.ware.mapper;

import neko.transaction.ware.entity.StockLockLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 库存锁定日志表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Mapper
public interface StockLockLogMapper extends BaseMapper<StockLockLog> {

}
