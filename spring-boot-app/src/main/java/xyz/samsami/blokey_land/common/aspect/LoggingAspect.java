package xyz.samsami.blokey_land.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* xyz.samsami.blokey_land..service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("메서드 시작: {} | 파라미터: {}", methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long end = System.currentTimeMillis();
            long duration = end - start;

            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long usedHeapMB = heapUsage.getUsed() / (1024 * 1024);
            long maxHeapMB = heapUsage.getMax() / (1024 * 1024);

            log.info(
                "메서드 종료: {} | 실행시간: {}ms | 힙 메모리 사용: {}MB/{}MB",
                methodName, duration, usedHeapMB, maxHeapMB
            );

            return result;
        } catch (Throwable ex) {
            long end = System.currentTimeMillis();
            long duration = end - start;
            log.error("예외 발생: {} | 실행 시간: {}ms | 예외 메시지: {}", methodName, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}