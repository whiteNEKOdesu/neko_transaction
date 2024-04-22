package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import neko.transaction.product.entity.AccusationInfo;
import neko.transaction.product.mapper.AccusationInfoMapper;
import neko.transaction.product.service.AccusationInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.product.vo.NewAccusationInfoVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 举报信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-04-22
 */
@Service
public class AccusationInfoServiceImpl extends ServiceImpl<AccusationInfoMapper, AccusationInfo> implements AccusationInfoService {

    /**
     * 添加举报信息
     * @param vo 添加举报信息vo
     */
    @Override
    public void newAccusationInfo(NewAccusationInfoVo vo) {
        AccusationInfo todoAdd = new AccusationInfo();
        BeanUtil.copyProperties(vo, todoAdd);
        String uid = StpUtil.getLoginId().toString();
        todoAdd.setInformerUid(uid);

        this.baseMapper.insert(todoAdd);
    }
}
