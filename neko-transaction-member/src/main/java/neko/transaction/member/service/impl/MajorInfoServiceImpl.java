package neko.transaction.member.service.impl;

import neko.transaction.member.entity.MajorInfo;
import neko.transaction.member.mapper.MajorInfoMapper;
import neko.transaction.member.service.MajorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
