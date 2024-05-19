package neko.transaction.member.mapper;

import neko.transaction.member.entity.ApiAuthInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiAuthInfoMapperTest {
    @Resource
    private ApiAuthInfoMapper apiAuthInfoMapper;

    @Test
    public void updateBatch(){
        System.out.println(apiAuthInfoMapper.updateBatch(Arrays.asList(new ApiAuthInfo().setApiId(1L)
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO"),
                new ApiAuthInfo().setApiId(2L)
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO"),
                new ApiAuthInfo().setApiId(3L)
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO"))));
    }

    @Test
    public void insertBatch(){
        apiAuthInfoMapper.insertBatch(Arrays.asList(new ApiAuthInfo().setPath("NEKO")
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO"),
                new ApiAuthInfo().setPath("NEKO")
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO"),
                new ApiAuthInfo().setPath("NEKO")
                        .setRequestMethod("NEKO")
                        .setHandlerMethod("NEKO")));
    }
}
