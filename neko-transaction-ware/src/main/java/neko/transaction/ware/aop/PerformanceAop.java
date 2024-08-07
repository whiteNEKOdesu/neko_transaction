package neko.transaction.ware.aop;

import lombok.extern.slf4j.Slf4j;
import neko.transaction.ware.config.ActiveValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 执行时间记录aop
 */
@Aspect
@Component
@Slf4j
public class PerformanceAop {
    //aop增强范围为被RestController注解修饰方法
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut(){

    }

    /**
     * 环绕方法，记录执行时间
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long cost = System.currentTimeMillis() - start;

        if(ActiveValue.isDebug){
            logExecute(cost, joinPoint, false);
        }else if(cost > 100){
            logExecute(cost, joinPoint, true);
        }

        return proceed;
    }

    private void logExecute(long cost, ProceedingJoinPoint joinPoint, boolean isWarn){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if(isWarn){
            log.warn("路径: " + request.getRequestURI() + "，方法: " + methodSignature.getName() + "，耗时: " + cost + "ms");
        }else{
            log.info("路径: " + request.getRequestURI() + "，方法: " + methodSignature.getName() + "，耗时: " + cost + "ms");
        }
    }
}
