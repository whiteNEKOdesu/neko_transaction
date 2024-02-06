package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberChatInfoMapperTest {
    @Resource
    private MemberChatInfoMapper memberChatInfoMapper;

    @Test
    public void userSelfPageQuery(){
        System.out.println(memberChatInfoMapper.memberChattingWithPageQuery(8,
                0,
                "1642067605873348610"));
    }

    @Test
    public void memberChattingWithPageQueryNumber(){
        System.out.println(memberChatInfoMapper.memberChattingWithPageQueryNumber("1642067605873348610"));
    }

    @Test
    public void getUnreadChatIdByFromIdToId(){
        System.out.println(memberChatInfoMapper.getUnreadChatIdByFromIdToId("2022djy", "1642067605873348610"));
    }
}
