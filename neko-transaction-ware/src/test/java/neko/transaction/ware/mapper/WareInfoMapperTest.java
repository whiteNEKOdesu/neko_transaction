package neko.transaction.ware.mapper;

import neko.transaction.ware.vo.LockStockVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
public class WareInfoMapperTest {
    @Resource
    private WareInfoMapper wareInfoMapper;

    @Test
    public void wareInfoById(){
        System.out.println(wareInfoMapper.wareInfoById("NEKO"));
    }

    @Test
    public void updateStockByProductId(){
        System.out.println(wareInfoMapper.updateStockByProductId("1749707857651134465", 5));
    }

    @Test
    public void lockStock(){
        System.out.println(wareInfoMapper.lockStock(1L, 5));
    }

    @Test
    public void lockStocks(){
        System.out.println(wareInfoMapper.lockStocks(Arrays.asList(new LockStockVo.LockProductInfo()
                        .setProductId("1750067945863770113")
                        .setLockNumber(5),
                new LockStockVo.LockProductInfo()
                        .setProductId("1750068049572130817")
                        .setLockNumber(5))));
    }

    @Test
    public void unlockStock(){
        System.out.println(wareInfoMapper.unlockStock(1L, 1L));
    }

    @Test
    public void decreaseStock(){
        wareInfoMapper.decreaseStock(1L, 5);
    }
}
