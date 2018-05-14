package by.troyan.aspect;

import by.troyan.strategy.jdbc.impl.InJdbcStrategyCountryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceChecker {
    private static final Logger LOG = LogManager.getLogger(InJdbcStrategyCountryImpl.class);

    @Around("@annotation(by.troyan.aspect.TimeCounted)")
    public Object checkPerformance(ProceedingJoinPoint proceedingJoinPoint){
        long start = System.nanoTime();
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            LOG.error("Exception: " + e);
        }
        long end = System.nanoTime();
        LOG.info("The performance of [" + proceedingJoinPoint.getSignature().getName()+
                "] method is: " + (end - start) + " milliseconds.");
        return value;
    }
}
