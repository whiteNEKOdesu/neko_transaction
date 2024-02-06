package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberChatInfoReadLogMapperTest {
    @Resource
    private MemberChatInfoReadLogMapper memberChatInfoReadLogMapper;

    @Test
    public void insertBatch(){
        memberChatInfoReadLogMapper.insertBatch(Arrays.asList(1L, 2L),
                "2022djy",
                "1642067605873348610");
    }
}
