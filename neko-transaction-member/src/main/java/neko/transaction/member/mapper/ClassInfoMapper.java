package neko.transaction.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.member.entity.ClassInfo;
import neko.transaction.member.vo.ClassInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 学生班级信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface ClassInfoMapper extends BaseMapper<ClassInfo> {
    /**
     * 分页查询班级信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @return 查询结果
     */
    List<ClassInfoVo> pageQuery(Integer limited,
                                Integer start,
                                String queryWords,
                                Integer collegeId);

    /**
     * 获取分页查询班级信息的结果总页数
     * @param queryWords 查询条件
     * @return 查询结果页数
     */
    int pageQueryNumber(String queryWords, Integer collegeId);
}
