package neko.transaction.ware.aop;

import cn.dev33.satoken.stp.StpUtil;
import neko.transaction.ware.entity.ApiAuthInfo;
import neko.transaction.ware.service.ApiAuthInfoService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 动态鉴权aop
 */
@Aspect
@Component
public class DynamicAuthAop {
    @Resource
    private ApiAuthInfoService apiAuthInfoService;

    //aop增强范围为被RestController注解修饰方法
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut(){

    }

    /**
     * 环绕方法，鉴权
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //动态鉴权
        auth();

        return joinPoint.proceed();
    }

    /**
     * 动态鉴权
     */
    private void auth(){
        //根据当前请求路径获取 api 鉴权信息
        ApiAuthInfo apiAuthInfo = apiAuthInfoService.getApiAuthInfoByCurrentRequest();
        if(apiAuthInfo == null){
            return;
        }

        if(apiAuthInfo.getRole() != null){
            StpUtil.checkRole(apiAuthInfo.getRole());
        }
        if(apiAuthInfo.getWeight() != null){
            StpUtil.checkPermission(apiAuthInfo.getWeight());
        }
    }
}
