package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MemberInfoMapperTest {
    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Test
    public void memberWithSchoolInfoPageQuery(){
        System.out.println(memberInfoMapper.memberWithSchoolInfoPageQuery(8,
                0,
                "",
                ""));
    }

    @Test
    public void memberWithSchoolInfoPageQueryNumber(){
        System.out.println(memberInfoMapper.memberWithSchoolInfoPageQueryNumber("", ""));
    }

    @Test
    public void getMemberInfoByUid(){
        System.out.println(memberInfoMapper.getMemberInfoByUid("1642067605873348610"));
    }
}
