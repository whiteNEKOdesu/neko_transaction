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
        System.out.println(productApplyInfoMapper.updateUnhandledApplyStatus("apply_1", Byte.valueOf("1")));
    }
}
