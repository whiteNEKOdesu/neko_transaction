package neko.transaction.ware.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class WareInfoMapperTest {
    @Resource
    private WareInfoMapper wareInfoMapper;

    @Test
    public void wareInfoById(){
        System.out.println(wareInfoMapper.wareInfoById("NEKO"));
    }
}
