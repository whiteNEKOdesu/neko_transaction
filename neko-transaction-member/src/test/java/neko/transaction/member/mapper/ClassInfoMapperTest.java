package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ClassInfoMapperTest {
    @Resource
    private ClassInfoMapper classInfoMapper;

    @Test
    public void pageQuery(){
        System.out.println(classInfoMapper.pageQuery(8,
                0,
                "软件",
                1));
    }

    @Test
    public void pageQueryNumber(){
        System.out.println(classInfoMapper.pageQueryNumber("软件", 1));
    }
}
