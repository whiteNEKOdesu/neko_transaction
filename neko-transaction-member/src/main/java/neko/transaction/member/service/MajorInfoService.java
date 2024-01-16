package neko.transaction.member.service;

import neko.transaction.member.entity.MajorInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.transaction.member.vo.NewMajorInfoVo;

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
}
