package neko.transaction.product.mapper;

import neko.transaction.product.entity.ReturnApplyImage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class ReturnApplyImageMapperTest {
    @Resource
    private ReturnApplyImageMapper returnApplyImageMapper;

    @Test
    public void insertBatch(){
        returnApplyImageMapper.insertBatch(Arrays.asList(new ReturnApplyImage().setApplyId(1L)
                        .setApplyImage("NEKO"),
                new ReturnApplyImage().setApplyId(1L)
                        .setApplyImage("NEKO")));
    }
}
