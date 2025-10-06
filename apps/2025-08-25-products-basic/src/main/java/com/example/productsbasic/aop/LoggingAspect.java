package com.example.productsbasic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.productsbasic.controller.*.*(..)) || execution(* com.example.productsbasic.service.*.*(..))")
    public void apiPerformanceTargets() {
    }

    @Around("apiPerformanceTargets()")
    public Object logServiceCompletion(ProceedingJoinPoint pjp) throws Throwable {
        final long startTime = System.currentTimeMillis();
        String methodName = pjp.getSignature().toShortString();
        String className = pjp.getSignature().getDeclaringTypeName();

        logger.info("[処理開始]: class={}, method={}", className, methodName);

        Object result = null;
        Throwable throwable = null;

        try {
            result = pjp.proceed();
            return result;

        } catch (Throwable t) {
            throwable = t;
            throw t;

        } finally {
            final long duration = System.currentTimeMillis() - startTime;
            if (throwable == null) {
                logger.info("[処理終了]: status=ok, class={}, method={}, processTime={}", className, methodName, duration);
            } else {
                logger.info("[処理終了]: status=ng, class={}, method={}, processTime={}", className, methodName, duration);
            }
        }
    }
}
