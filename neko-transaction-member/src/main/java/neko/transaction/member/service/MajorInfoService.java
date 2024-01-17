package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.MajorInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.FullMajorNameVo;
import neko.transaction.member.vo.MajorInfoVo;
import neko.transaction.member.vo.NewMajorInfoVo;

import java.util.List;

/**
 * <p>
 * 学生专业信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface MajorInfoService extends IService<MajorInfo> {
    /**
     * 添加专业信息
     * @param vo 添加专业信息vo
     */
    void newMajorInfo(NewMajorInfoVo vo);

    /**
     * 分页查询专业信息
     * @param vo 分页查询vo
     * @return 分页查询结果
     */
    Page<MajorInfoVo> majorInfoPageQuery(QueryVo vo);

    /**
     * 根据专业id删除专业信息
     * @param majorId 专业id
     */
    void deleteById(Integer majorId);

    /**
     * 获取所有完整专业名信息
     * @return 所有完整专业名信息
     */
    List<FullMajorNameVo> getAllFullMajorName();
}
