package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRoleMapperTest {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Test
    public void getMemberLevelRoleNumberByRoleIds(){
        System.out.println(userRoleMapper.getMemberLevelRoleNumberByRoleIds(Arrays.asList(1, 2)));
    }
}
