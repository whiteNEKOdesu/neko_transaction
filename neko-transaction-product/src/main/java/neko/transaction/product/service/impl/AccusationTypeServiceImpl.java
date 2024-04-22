package neko.transaction.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import neko.transaction.product.entity.AccusationType;
import neko.transaction.product.mapper.AccusationTypeMapper;
import neko.transaction.product.service.AccusationTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewAccusationTypeVo;
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

    /**
     * 添加举报类型信息
     * @param vo 添加举报类型信息vo
     */
    @Override
    public void newAccusationType(NewAccusationTypeVo vo) {
        AccusationType todoAdd = new AccusationType();
        BeanUtil.copyProperties(vo, todoAdd);

        this.baseMapper.insert(todoAdd);
    }
}
