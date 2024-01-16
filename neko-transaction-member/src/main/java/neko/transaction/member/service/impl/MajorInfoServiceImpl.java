package neko.transaction.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.CollegeInfo;
import neko.transaction.member.entity.MajorInfo;
import neko.transaction.member.mapper.MajorInfoMapper;
import neko.transaction.member.service.MajorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.vo.NewMajorInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 学生专业信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class MajorInfoServiceImpl extends ServiceImpl<MajorInfoMapper, MajorInfo> implements MajorInfoService {

    /**
     * 添加专业信息
     * @param vo 添加专业信息vo
     */
    @Override
    public void newMajorInfo(NewMajorInfoVo vo) {
        MajorInfo majorInfo = new MajorInfo();
        BeanUtil.copyProperties(vo, majorInfo);

        //添加专业信息
        this.baseMapper.insert(majorInfo);
    }

    /**
     * 分页查询专业信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    @Override
    public Page<MajorInfo> majorInfoPageQuery(QueryVo vo) {
        Page<MajorInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<MajorInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            //拼接查询条件
            queryWrapper.lambda().like(MajorInfo::getMajorName, vo.getQueryWords());
        }
        queryWrapper.lambda().orderByDesc(MajorInfo::getMajorId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
