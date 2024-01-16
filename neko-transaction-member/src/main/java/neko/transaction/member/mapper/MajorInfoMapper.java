package neko.transaction.member.mapper;

import neko.transaction.member.entity.MajorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.transaction.member.vo.MajorInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 学生专业信息表 Mapper 接口
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Mapper
public interface MajorInfoMapper extends BaseMapper<MajorInfo> {
    /**
     * 分页查询专业信息
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @return 查询结果
     */
    List<MajorInfoVo> pageQuery(Integer limited,
                                Integer start,
                                String queryWords);

    /**
     * 分页查询专业信息的页数
     * @param limited 每页数量
     * @param start 起始位置
     * @param queryWords 查询条件
     * @return 查询结果页数
     */
    int pageQueryNumber(Integer limited,
                                Integer start,
                                String queryWords);
}
