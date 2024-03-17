package neko.transaction.member.mapper;

import neko.transaction.member.entity.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    public void getPublicMemberInfoByUid(){
        System.out.println(memberInfoMapper.getPublicMemberInfoByUid("1642067605873348610"));
    }

    @Test
    public void addBalance(){
        memberInfoMapper.addBalance("1642067605873348610",
                new BigDecimal("15"));
    }

    @Test
    public void insertBatch(){
        System.out.println(memberInfoMapper.insertBatch(Arrays.asList(new MemberInfo().setUid("NEKO_1")
                        .setClassId("z1312202")
                        .setUserPassword("NEKO")
                        .setSalt("NEKO")
                        .setGender(true)
                        .setRealName("NEKO")
                        .setIdCardNumber("NEKO_1"),
                new MemberInfo().setUid("NEKO_2")
                        .setClassId("z1312202")
                        .setUserPassword("NEKO")
                        .setSalt("NEKO")
                        .setGender(true)
                        .setRealName("NEKO")
                        .setIdCardNumber("NEKO_2"),
                new MemberInfo().setUid("NEKO_3")
                        .setClassId("z1312202")
                        .setUserPassword("NEKO")
                        .setSalt("NEKO")
                        .setGender(true)
                        .setRealName("NEKO")
                        .setIdCardNumber("NEKO_3"))));
    }
}
