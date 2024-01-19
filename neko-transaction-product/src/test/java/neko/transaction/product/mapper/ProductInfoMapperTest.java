package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ProductInfoMapperTest {
    @Resource
    private ProductInfoMapper productInfoMapper;

    @Test
    public void userSelfPageQuery(){
        System.out.println(productInfoMapper.userSelfPageQuery(8,
                0,
                "插画",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void userSelfPageQueryNumber(){
        System.out.println(productInfoMapper.userSelfPageQueryNumber("插画",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void getUserSelfProductInfoById(){
        System.out.println(productInfoMapper.getUserSelfProductInfoById("1748171551460679681",
                "1642067605873348610"));
    }
}
