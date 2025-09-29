package com.example.productsbasic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceCompletion(ProceedingJoinPoint pjp) throws Throwable {
        final long startNs = System.nanoTime();
        final String sig = pjp.getSignature().toShortString();

        try {
            Object ret = pjp.proceed();
            long tookMs = (System.nanoTime() - startNs) / 1_000_000;
            logger.info("OK sig={} took_ms={}", sig, tookMs);
            return ret;
        } catch (Throwable ex) {
            long tookMs = (System.nanoTime() - startNs) / 1_000_000;
            logger.error("NG sig={} took_ms={} exType={} msg={}", sig, tookMs, ex.getClass().getSimpleName(),
                    ex.getMessage(), ex);
            throw ex;
        }
    }
}
