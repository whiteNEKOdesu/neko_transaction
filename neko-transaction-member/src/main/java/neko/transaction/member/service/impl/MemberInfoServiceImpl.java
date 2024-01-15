package neko.transaction.member.service.impl;

import neko.transaction.member.entity.MemberInfo;
import neko.transaction.member.mapper.MemberInfoMapper;
import neko.transaction.member.service.MemberInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo> implements MemberInfoService {

}
