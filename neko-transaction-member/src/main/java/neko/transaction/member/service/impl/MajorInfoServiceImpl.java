package neko.transaction.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.MajorInfo;
import neko.transaction.member.mapper.MajorInfoMapper;
import neko.transaction.member.service.MajorInfoService;
import neko.transaction.member.vo.FullMajorNameVo;
import neko.transaction.member.vo.MajorInfoVo;
import neko.transaction.member.vo.NewMajorInfoVo;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Page<MajorInfoVo> majorInfoPageQuery(QueryVo vo) {
        Page<MajorInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        //设置分页查询结果
        page.setRecords(this.baseMapper.pageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords()));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.pageQueryNumber(vo.getQueryWords()));

        return page;
    }

    /**
     * 根据专业id删除专业信息
     * @param majorId 专业id
     */
    @Override
    public void deleteById(Integer majorId) {
        this.baseMapper.deleteById(majorId);
    }

    /**
     * 获取所有完整专业名信息
     * @return 所有完整专业名信息
     */
    @Override
    public List<FullMajorNameVo> getAllFullMajorName() {
        return this.baseMapper.getAllFullMajorName();
    }
}
