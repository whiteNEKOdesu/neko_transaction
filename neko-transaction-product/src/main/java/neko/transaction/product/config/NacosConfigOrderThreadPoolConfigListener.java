package neko.transaction.product.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * nacos 配置中心 order 线程池配置监听配置
 */
@Component
@Slf4j
public class NacosConfigOrderThreadPoolConfigListener implements ApplicationRunner {
    @Resource
    private NacosConfigManager nacosConfigManager;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Resource(name = "threadPoolExecutor")
    private ThreadPoolExecutor threadPool;

    /**
     * 订单线程池配置文件在 nacos 配置中心的 dataId
     */
    private String dataId;

    /**
     * 订单线程池配置文件在 nacos 配置中心的 分组
     */
    private String group;

    @PostConstruct
    public void init(){
        List<NacosConfigProperties.Config> sharedConfigs = nacosConfigProperties.getSharedConfigs();
        Assert.isTrue(sharedConfigs != null && !sharedConfigs.isEmpty(),
                "配置: spring.cloud.nacos.config.shared-configs 未配置，请进行配置，下标 0 为订单线程池 nacos 配置中心监听配置");

        NacosConfigProperties.Config config = sharedConfigs.get(0);

        dataId = config.getDataId();
        group = config.getGroup();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        nacosConfigManager.getConfigService().addListener(dataId, group, new PropertiesListener() {
            @Override
            public void innerReceive(Properties properties) {
                //线程池参数修改
                refreshThreadPool(properties);
            }
        });
    }

    /**
     * 线程池参数修改
     * @param properties 线程池配置参数
     */
    private void refreshThreadPool(Properties properties){
        for(Map.Entry<Object,Object> entry : properties.entrySet()){
            String key = entry.getKey().toString(), value = entry.getValue().toString();
            if(!StringUtils.hasText(key) || !StringUtils.hasText(value)){
                continue;
            }

            //备份原配置
            int corePoolSize = threadPool.getCorePoolSize();
            int maximumPoolSize = threadPool.getMaximumPoolSize();
            long keepAliveTime = threadPool.getKeepAliveTime(TimeUnit.SECONDS);

            try {
                if("core-size".equals(key)){
                    threadPool.setCorePoolSize(Integer.parseInt(value));
                }else if("max-size".equals(key)){
                    threadPool.setMaximumPoolSize(Integer.parseInt(value));
                }else if("keep-alive-time".equals(key)){
                    threadPool.setKeepAliveTime(Long.parseLong(value), TimeUnit.SECONDS);
                }
            }catch (Exception e){
                log.error("订单线程池动态配置异常: " + e);

                //恢复原配置
                threadPool.setCorePoolSize(corePoolSize);
                threadPool.setMaximumPoolSize(maximumPoolSize);
                threadPool.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);

                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("订单线程池动态配置修改，")
                    .append("core-size: ")
                    .append(threadPool.getCorePoolSize())
                    .append("，")
                    .append("max-size: ")
                    .append(threadPool.getMaximumPoolSize())
                    .append("，")
                    .append("keep-alive-time: ")
                    .append(threadPool.getKeepAliveTime(TimeUnit.SECONDS));

            log.info(stringBuilder.toString());
        }
    }
}
