package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class CategoryInfoMapperTest {
    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    @Test
    public void getFullCategoryName(){
        System.out.println(categoryInfoMapper.getFullCategoryName(17));
    }
}
