package by.eugenekulik.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Slf4j
public class TimedAspect {
    @Pointcut("@within(by.eugenekulik.service.annotation.Timed) && execution(* *(..))")
    public void callTimedMethod() {
    }

    @Around("callTimedMethod()")
    public Object timed(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(".").append(joinPoint.getSignature().getName());
        builder.append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        long before = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long after = System.currentTimeMillis();
        builder.append("execution time: ").append(after - before).append("ms");
        log.info(builder.toString());
        return retVal;
    }
}
