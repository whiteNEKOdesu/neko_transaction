package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class OrderInfoMapperTest {
    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Test
    public void updateOrderInfoStatusToCancel(){
        orderInfoMapper.updateOrderInfoStatusToCancel("202401311052224051752525198848925698");
    }

    @Test
    public void userSelfPageQuery(){
        System.out.println(orderInfoMapper.userSelfPageQuery(8,
                0,
                "",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void userSelfPageQueryNumber(){
        System.out.println(orderInfoMapper.userSelfPageQueryNumber("",
                "1642067605873348610",
                Byte.valueOf("0")));
    }

    @Test
    public void statusAggCount(){
        System.out.println(orderInfoMapper.statusAggCount());
    }
}
