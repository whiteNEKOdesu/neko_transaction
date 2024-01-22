package neko.transaction.ware.service.impl;

import neko.transaction.ware.entity.StockUpdateLog;
import neko.transaction.ware.mapper.StockUpdateLogMapper;
import neko.transaction.ware.service.StockUpdateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存更新日志表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-22
 */
@Service
public class StockUpdateLogServiceImpl extends ServiceImpl<StockUpdateLogMapper, StockUpdateLog> implements StockUpdateLogService {

}
