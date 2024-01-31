package neko.transaction.product.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * nacos 配置中心 order 线程池配置监听配置
 */
@Component
public class NacosConfigOrderThreadPoolConfigListener implements ApplicationRunner {
    @Resource
    private NacosConfigManager nacosConfigManager;

    @Value("${spring.cloud.nacos.config.group}")
    private String group;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String dataId = "order-dynamic-thread-pool.yaml";
        nacosConfigManager.getConfigService().addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                System.out.println(properties);
            }
        });
    }
}
