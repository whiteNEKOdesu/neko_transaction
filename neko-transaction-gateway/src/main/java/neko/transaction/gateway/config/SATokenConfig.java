package neko.transaction.gateway.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import neko.transaction.gateway.entity.Response;
import neko.transaction.gateway.entity.ResultObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SATokenConfig {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                //拦截地址
                .addInclude("/member/user_role_relation/new_user_role_relation")
                .addInclude("/third_party/mail/send_register_mail")
                .addInclude("/member/weight_role_relation/relation_info_by_uid")
                .addInclude("/product/sku_info/sku_id_market_info")
                .addInclude("/product/sku_info/product_infos")
                .addInclude("/ware/ware_info/lock_stock")
                .addInclude("/ware/ware_info/unlock_stock")
                .addInclude("/ware/stock_lock_log/order_record_sku_id_infos")
                .addInclude("/ware/ware_info/confirm_lock_stock_pay")
                .addInclude("/member/member_info/real_name_info")
                .addInclude("/third_party/oss/upload_image")
                .addInclude("/third_party/oss/delete_file")
                .addInclude("/ware/ware_info/new_ware_info")
                .addInclude("/product/order_info/remote_invoke_order_info_by_id")
                .setAuth(obj -> {
                    StpUtil.checkRole("*");
                })
                //异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    return new ResultObject<Object>().setResponseStatus(Response.TOKEN_CHECK_ERROR)
                            .compact();
                });
    }
}
