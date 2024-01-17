package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.ClassInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.ClassInfoVo;
import neko.transaction.member.vo.NewClassInfoVo;

/**
 * <p>
 * 学生班级信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface ClassInfoService extends IService<ClassInfo> {
    /**
     * 分页查询班级信息
     * @param vo 分页查询vo
     * @return 查询结果
     */
    Page<ClassInfoVo> pageQuery(QueryVo vo);

    /**
     * 添加班级信息
     * @param vo 添加班级信息的vo
     */
    void newClassInfo(NewClassInfoVo vo);
}
