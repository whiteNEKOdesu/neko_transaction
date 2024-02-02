package neko.transaction.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置
 */
@Configuration
public class WebSocketConfig {
    /**
     * 	注入ServerEndpointExporter，这个bean会自动注册
     * 	使用了 @ServerEndpoint 注解声明的 Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
