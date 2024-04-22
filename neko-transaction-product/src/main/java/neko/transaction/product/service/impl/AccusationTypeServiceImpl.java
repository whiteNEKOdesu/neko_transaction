package neko.transaction.product.service.impl;

import neko.transaction.product.entity.AccusationType;
import neko.transaction.product.mapper.AccusationTypeMapper;
import neko.transaction.product.service.AccusationTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 举报类型信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Service
public class AccusationTypeServiceImpl extends ServiceImpl<AccusationTypeMapper, AccusationType> implements AccusationTypeService {

}
