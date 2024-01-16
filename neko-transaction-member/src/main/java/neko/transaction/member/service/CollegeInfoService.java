package neko.transaction.member.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.transaction.commonbase.utils.entity.QueryVo;
import neko.transaction.member.entity.CollegeInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 二级学院信息表 服务类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
public interface CollegeInfoService extends IService<CollegeInfo> {
    /**
     * 添加二级学院
     * @param collegeName 二级学院名
     */
    void newCollege(String collegeName);

    /**
     * 分页查询二级学院信息
     * @param vo 分页查询vo
     * @return 二级学院分页信息
     */
    Page<CollegeInfo> collegeInfoPageQuery(QueryVo vo);
}
