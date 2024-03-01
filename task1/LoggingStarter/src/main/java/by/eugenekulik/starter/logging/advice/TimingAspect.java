package by.eugenekulik.starter.logging.advice;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Aspect
@Slf4j
public class TimingAspect {


    @Around("by.eugenekulik.starter.logging.pointcut.Pointcuts.callTimedMethod()")
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
