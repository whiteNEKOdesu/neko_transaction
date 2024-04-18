package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MqReturnMessageMapperTest {
    @Resource
    private MqReturnMessageMapper mqReturnMessageMapper;

    @Test
    public void deleteMqReturnMessageByMqReturnIds(){
        mqReturnMessageMapper.deleteMqReturnMessageByMqReturnIds(Arrays.asList(1L, 2L));
    }
}
