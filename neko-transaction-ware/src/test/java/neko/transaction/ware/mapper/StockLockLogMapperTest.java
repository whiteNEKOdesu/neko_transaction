package neko.transaction.ware.mapper;

import neko.transaction.ware.vo.LockStockVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class StockLockLogMapperTest {
    @Resource
    private StockLockLogMapper stockLockLogMapper;

    @Test
    public void updateStatusToCancelLock(){
        stockLockLogMapper.updateStatusToCancelLock("202401311052224051752525198848925698");
    }

    @Test
    public void updateStatusToPaid(){
        stockLockLogMapper.updateStatusToPaid("202401311521066471752592828817457153");
    }

    @Test
    public void insertBatch(){
        stockLockLogMapper.insertBatch("202401311521066471752592828817457153",
                Arrays.asList(new LockStockVo.LockProductInfo()
                                .setProductId("1750067945863770113")
                                .setLockNumber(5),
                        new LockStockVo.LockProductInfo()
                                .setProductId("1750068049572130817")
                                .setLockNumber(5)));
    }
}
