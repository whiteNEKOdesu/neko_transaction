package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class OrderDetailInfoMapperTest {
    @Resource
    private OrderDetailInfoMapper orderDetailInfoMapper;

    @Test
    public void getByIdUid(){
        System.out.println(orderDetailInfoMapper.getByIdUid("1752936153697193985",
                "1642067605873348610"));
    }

    @Test
    public void sellerSelfPageQuery(){
        System.out.println(orderDetailInfoMapper.sellerSelfPageQuery(8,
                0,
                "插",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void sellerSelfPageQueryNumber(){
        System.out.println(orderDetailInfoMapper.sellerSelfPageQueryNumber("插",
                "1642067605873348610",
                Byte.valueOf("0")));
    }
}
