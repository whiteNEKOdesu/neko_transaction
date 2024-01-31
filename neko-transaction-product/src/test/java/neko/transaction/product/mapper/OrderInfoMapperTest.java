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
}
