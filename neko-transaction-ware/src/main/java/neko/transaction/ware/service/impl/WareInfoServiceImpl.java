package neko.transaction.ware.service.impl;

import neko.transaction.ware.entity.WareInfo;
import neko.transaction.ware.mapper.WareInfoMapper;
import neko.transaction.ware.service.WareInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
