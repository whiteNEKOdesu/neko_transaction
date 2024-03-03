package neko.transaction.product.config;

import neko.transaction.commonbase.utils.entity.ProfilesActive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 激活的运行环境
 */
@Component
public class ActiveValue {
    @Value("${spring.profiles.active}")
    public String active;

    public static Boolean isDebug = true;

    @PostConstruct
    public void init(){
        if(ProfilesActive.PROP.equals(active)){
            isDebug = false;
        }
    }

    public String getActive(){
        return active;
    }
}
