package neko.transaction.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.AccusationType;
import neko.transaction.product.mapper.AccusationTypeMapper;
import neko.transaction.product.service.AccusationTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewAccusationTypeVo;
import neko.transaction.product.vo.UpdateAccusationTypeVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    /**
     * 分页查询举报类型信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<AccusationType> accusationTypePageQuery(QueryVo vo) {
        Page<AccusationType> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<AccusationType> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            //拼接查询条件
            queryWrapper.lambda().like(AccusationType::getAccuseType, vo.getQueryWords());
        }
        queryWrapper.lambda().orderByDesc(AccusationType::getSort)
                .orderByDesc(AccusationType::getAccuseTypeId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }

    /**
     * 修改举报类型信息
     * @param vo 修改举报类型信息vo
     */
    @Override
    public void updateAccusationType(UpdateAccusationTypeVo vo) {
        AccusationType todoUpdate = new AccusationType();
        BeanUtil.copyProperties(vo, todoUpdate);

        this.baseMapper.updateById(todoUpdate);
    }

    /**
     * 删除举报类型
     * @param accuseTypeId 举报类型id
     */
    @Override
    public void deleteAccusationType(Integer accuseTypeId) {
        this.baseMapper.deleteById(accuseTypeId);
    }
}
