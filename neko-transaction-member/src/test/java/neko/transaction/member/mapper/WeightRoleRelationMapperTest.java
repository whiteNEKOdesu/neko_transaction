package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeightRoleRelationMapperTest {
    @Resource
    private WeightRoleRelationMapper weightRoleRelationMapper;

    @Test
    public void getRelationsByRoleIds(){
        System.out.println(weightRoleRelationMapper.getRelationsByRoleIds(Arrays.asList(1, 2)));
    }

    @Test
    public void getRelationSbyRoleId(){
        System.out.println(weightRoleRelationMapper.getRelationSbyRoleId(1));
    }
}
