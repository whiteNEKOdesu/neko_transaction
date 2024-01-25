package neko.transaction.product;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@EnableFeignClients
@EnableAutoDataSourceProxy
public class NekoTransactionProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(NekoTransactionProductApplication.class, args);
    }

}
