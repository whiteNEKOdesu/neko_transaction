package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ProductApplyInfoMapperTest {
    @Resource
    private ProductApplyInfoMapper productApplyInfoMapper;

    @Test
    public void unhandledApplyPageQuery(){
        System.out.println(productApplyInfoMapper.unhandledApplyPageQuery(8,
                0,
                "NEKO"));
    }

    @Test
    public void unhandledApplyPageQueryNumber(){
        System.out.println(productApplyInfoMapper.unhandledApplyPageQueryNumber("NEKO"));
    }

    @Test
    public void updateUnhandledApplyStatus(){
        System.out.println(productApplyInfoMapper.updateUnhandledApplyStatus(1L, Byte.valueOf("1")));
    }

    @Test
    public void userSelfApplyPageQuery(){
        System.out.println(productApplyInfoMapper.userSelfApplyPageQuery(8,
                0,
                "NEKO",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void userSelfApplyPageQueryNumber(){
        System.out.println(productApplyInfoMapper.userSelfApplyPageQueryNumber("NEKO",
                "1642067605873348610",
                Byte.valueOf("0")));
    }
}
