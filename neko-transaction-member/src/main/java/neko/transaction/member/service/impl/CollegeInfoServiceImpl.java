package neko.transaction.member.service.impl;

import neko.transaction.member.entity.CollegeInfo;
import neko.transaction.member.mapper.CollegeInfoMapper;
import neko.transaction.member.service.CollegeInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 二级学院信息表 服务实现类
 * </p>
 *
 * @author NEKO
 * @since 2024-01-15
 */
@Service
public class CollegeInfoServiceImpl extends ServiceImpl<CollegeInfoMapper, CollegeInfo> implements CollegeInfoService {

}
