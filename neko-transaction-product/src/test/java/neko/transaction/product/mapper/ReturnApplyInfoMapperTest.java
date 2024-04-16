package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ReturnApplyInfoMapperTest {
    @Resource
    private ReturnApplyInfoMapper returnApplyInfoMapper;

    @Test
    public void getReturnApplyInfoByOrderDetailId(){
        System.out.println(returnApplyInfoMapper.getReturnApplyInfoByOrderDetailId("1768149097477259265"));
    }

    @Test
    public void returnApplyInfoPageQuery(){
        System.out.println(returnApplyInfoMapper.returnApplyInfoPageQuery(8,
                0,
                "插画",
                Byte.valueOf("0")));
    }

    @Test
    public void returnApplyInfoPageQueryNumber(){
        System.out.println(returnApplyInfoMapper.returnApplyInfoPageQueryNumber("插画",
                Byte.valueOf("0")));
    }
}
