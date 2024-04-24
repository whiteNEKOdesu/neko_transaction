package neko.transaction.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.AccusationStatus;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.product.entity.AccusationInfo;
import neko.transaction.product.mapper.AccusationInfoMapper;
import neko.transaction.product.service.AccusationInfoService;
import neko.transaction.product.service.ProductInfoService;
import neko.transaction.product.vo.AccusationInfoVo;
import neko.transaction.product.vo.NewAccusationInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
    @Resource
    private ProductInfoService productInfoService;

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

    /**
     * 分页查询举报信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<AccusationInfoVo> unhandledAccusationInfoPageQuery(QueryVo vo) {
        Page<AccusationInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        Object objectId = vo.getObjectId();
        Integer accuseTypeId = objectId != null ? Integer.valueOf(objectId.toString()) : null;
        //设置分页查询结果
        page.setRecords(this.baseMapper.unhandledAccusationInfoPageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                accuseTypeId));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.unhandledAccusationInfoPageQueryNumber(vo.getQueryWords(),
                accuseTypeId));

        return page;
    }

    /**
     * 驳回举报
     * @param accuseId 举报id
     */
    @Override
    public void rejectAccusation(Long accuseId) {
        String uid = StpUtil.getLoginId().toString();

        AccusationInfo todoUpdate = new AccusationInfo();
        todoUpdate.setStatus(AccusationStatus.UNBANNED)
                .setOperationAdminId(uid);

        this.baseMapper.update(todoUpdate, new QueryWrapper<AccusationInfo>().lambda()
                .eq(AccusationInfo::getAccuseId, accuseId)
                .eq(AccusationInfo::getStatus, AccusationStatus.UNHANDLED));
    }

    /**
     * 通过举报
     * @param accuseId 举报id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passAccusation(Long accuseId) {
        AccusationInfo accusationInfo = this.baseMapper.selectById(accuseId);
        if(accusationInfo == null || !AccusationStatus.UNHANDLED.equals(accusationInfo.getStatus())){
            return;
        }

        //step1 -> 修改举报状态为封禁状态
        String uid = StpUtil.getLoginId().toString();

        AccusationInfo todoUpdate = new AccusationInfo();
        todoUpdate.setStatus(AccusationStatus.BANNED)
                .setOperationAdminId(uid);

        this.baseMapper.update(todoUpdate, new QueryWrapper<AccusationInfo>().lambda()
                .eq(AccusationInfo::getAccuseId, accuseId)
                .eq(AccusationInfo::getStatus, AccusationStatus.UNHANDLED));

        //step2 -> 封禁商品
        productInfoService.banProduct(accusationInfo.getProductId());
    }

    /**
     * 获取封禁原因
     * @param productId 商品id
     * @return 封禁原因
     */
    @Override
    public String getBanReason(String productId) {
        return this.baseMapper.getBanReason(productId);
    }
}
