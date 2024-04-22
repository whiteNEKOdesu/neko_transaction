package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AccusationInfoMapperTest {
    @Resource
    private AccusationInfoMapper accusationInfoMapper;

    @Test
    public void unhandledAccusationInfoPageQuery(){
        System.out.println(accusationInfoMapper.unhandledAccusationInfoPageQuery(8,
                0,
                "图解",
                4));
    }
}
