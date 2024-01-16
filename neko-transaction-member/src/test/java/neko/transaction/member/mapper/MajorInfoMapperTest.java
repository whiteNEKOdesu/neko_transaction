package neko.transaction.member.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MajorInfoMapperTest {
    @Resource
    private MajorInfoMapper majorInfoMapper;

    @Test
    public void pageQuery(){
        System.out.println(majorInfoMapper.pageQuery(8, 0, "软件"));
    }
}
