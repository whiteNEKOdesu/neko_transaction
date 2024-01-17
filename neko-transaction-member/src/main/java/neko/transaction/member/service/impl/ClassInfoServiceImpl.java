package neko.transaction.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.ClassInfo;
import neko.transaction.member.mapper.ClassInfoMapper;
import neko.transaction.member.service.ClassInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.transaction.member.vo.ClassInfoVo;
import neko.transaction.member.vo.MajorInfoVo;
import neko.transaction.member.vo.NewClassInfoVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生班级信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {

    /**
     * 分页查询班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    @Override
    public Page<ClassInfoVo> pageQuery(QueryVo vo) {
        Page<ClassInfoVo> page = new Page<>(vo.getCurrentPage(), vo.getLimited());
        Integer collegeId = (Integer) vo.getObjectId();
        //设置分页查询结果
        page.setRecords(this.baseMapper.pageQuery(vo.getLimited(),
                vo.daoPage(),
                vo.getQueryWords(),
                collegeId));
        //设置分页查询总页数
        page.setTotal(this.baseMapper.pageQueryNumber(vo.getQueryWords(), collegeId));

        return page;
    }

    /**
     * 添加班级信息
     * @param vo 添加班级信息的vo
     */
    @Override
    public void newClassInfo(NewClassInfoVo vo) {
        ClassInfo classInfo = new ClassInfo();
        BeanUtil.copyProperties(vo, classInfo);

        //添加班级信息
        this.baseMapper.insert(classInfo);
    }

    /**
     * 根据班级id删除班级信息
     * @param classId 班级id
     */
    @Override
    public void deleteById(String classId) {
        this.baseMapper.deleteById(classId);
    }
}
