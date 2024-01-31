package neko.transaction.ware.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class StockLockLogMapperTest {
    @Resource
    private StockLockLogMapper stockLockLogMapper;

    @Test
    public void updateStatusToCancelLock(){
        stockLockLogMapper.updateStatusToCancelLock("202401311052224051752525198848925698");
    }
}
