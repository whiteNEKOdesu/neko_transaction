package neko.transaction.ware.service.impl;

import neko.transaction.ware.entity.StockLockLog;
import neko.transaction.ware.mapper.StockLockLogMapper;
import neko.transaction.ware.service.StockLockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存锁定日志表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Service
public class StockLockLogServiceImpl extends ServiceImpl<StockLockLogMapper, StockLockLog> implements StockLockLogService {

}
