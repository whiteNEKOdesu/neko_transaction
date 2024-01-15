package neko.transaction.member.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("neko.transaction.rsa")
@Component
@Data
@Accessors(chain = true)
public class RSAConfigProperties {
    private String publicKey;

    private String privateKey;
}
