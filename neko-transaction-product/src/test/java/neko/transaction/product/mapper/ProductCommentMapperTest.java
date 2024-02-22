package neko.transaction.product.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ProductCommentMapperTest {
    @Resource
    private ProductCommentMapper productCommentMapper;

    @Test
    public void getProductScoreByProductId(){
        System.out.println(productCommentMapper.getProductScoreByProductId("1755862522365231105"));
    }
}
