package neko.transaction.ware;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoDataSourceProxy
public class NekoTransactionWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(NekoTransactionWareApplication.class, args);
    }

}
