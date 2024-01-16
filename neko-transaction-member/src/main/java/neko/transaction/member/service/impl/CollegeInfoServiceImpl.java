package neko.transaction.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.CollegeInfo;
import neko.transaction.member.mapper.CollegeInfoMapper;
import neko.transaction.member.service.CollegeInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 二级学院信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class CollegeInfoServiceImpl extends ServiceImpl<CollegeInfoMapper, CollegeInfo> implements CollegeInfoService {

    /**
     * 添加二级学院
     * @param collegeName 二级学院名
     */
    @Override
    public void newCollege(String collegeName) {
        if(!StringUtils.hasText(collegeName)){
            throw new IllegalArgumentException("学院名为空");
        }

        //去除前导后导空格
        collegeName = collegeName.trim();
        CollegeInfo collegeInfo = new CollegeInfo();
        collegeInfo.setCollegeName(collegeName);

        //添加二级学院信息
        this.baseMapper.insert(collegeInfo);
    }

    /**
     * 分页查询二级学院信息
     * @param vo 分页查询vo
     * @return 二级学院分页信息
     */
    @Override
    public Page<CollegeInfo> collegeInfoPageQuery(QueryVo vo) {
        Page<CollegeInfo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        QueryWrapper<CollegeInfo> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(vo.getQueryWords())){
            //拼接查询条件
            queryWrapper.lambda().like(CollegeInfo::getCollegeName, vo.getQueryWords());
        }
        queryWrapper.lambda().orderByDesc(CollegeInfo::getCollegeId);

        //分页查询
        this.baseMapper.selectPage(page, queryWrapper);

        return page;
    }
}
