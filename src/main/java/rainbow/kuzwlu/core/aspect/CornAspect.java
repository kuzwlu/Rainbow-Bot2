package rainbow.kuzwlu.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import rainbow.kuzwlu.core.plugins.annotation.Cron;

@Component
@Aspect
@Slf4j
public class CornAspect {

    @Pointcut(value = "@annotation(rainbow.kuzwlu.core.plugins.annotation.Cron)")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("aop 定时任务启动");
    }

    @Around("@annotation(cron)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Cron cron) throws Throwable {
        log.debug("aop 定时任务环绕");
        Signature signature = proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        Object target = proceedingJoinPoint.getTarget();
        Object thiss = proceedingJoinPoint.getThis();
        String kind = proceedingJoinPoint.getKind();
        System.out.println("king:"+kind);
        return proceedingJoinPoint.proceed();
    }

}
