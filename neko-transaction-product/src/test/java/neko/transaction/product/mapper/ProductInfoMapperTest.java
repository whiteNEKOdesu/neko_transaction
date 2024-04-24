package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

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

    @Test
    public void downProduct(){
        productInfoMapper.downProduct("1750067945863770113", "1642067605873348610");
    }

    @Test
    public void getUpProductDetailInfo(){
        System.out.println(productInfoMapper.getUpProductDetailInfo("1750067945863770113"));
    }

    @Test
    public void getProductDetailInfoByIds(){
        System.out.println(productInfoMapper.getProductDetailInfoByIds(Arrays.asList("1750067945863770113",
                "1750068049572130817")));
    }

    @Test
    public void increaseSaleNumber(){
        productInfoMapper.increaseSaleNumber("1750067945863770113", 1L);
    }

    @Test
    public void banProduct(){
        productInfoMapper.banProduct("1779335044930809857");
    }
}
