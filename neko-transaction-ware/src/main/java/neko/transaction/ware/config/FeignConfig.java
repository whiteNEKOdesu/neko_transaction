package neko.transaction.ware.config;

import feign.RequestInterceptor;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * feign全局事务id传递配置
 */
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> {
            //获取全局事务id
            String xid = RootContext.getXID();
            if(StringUtils.hasText(xid)){
                //添加全局事务id
                template.header(RootContext.KEY_XID, xid);
            }
        };
    }
}
