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

    @Test
    public void unhandledAccusationInfoPageQueryNumber(){
        System.out.println(accusationInfoMapper.unhandledAccusationInfoPageQueryNumber("图解",
                4));
    }

    @Test
    public void getBanReason(){
        System.out.println(accusationInfoMapper.getBanReason("1779335044930809857"));
    }
}
