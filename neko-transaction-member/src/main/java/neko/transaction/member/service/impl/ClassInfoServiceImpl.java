package neko.transaction.member.service.impl;

import neko.transaction.member.entity.ClassInfo;
import neko.transaction.member.mapper.ClassInfoMapper;
import neko.transaction.member.service.ClassInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
