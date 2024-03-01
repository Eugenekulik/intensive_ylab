package by.eugenekulik.starter.audit.service;

import org.aspectj.lang.JoinPoint;


public class DefaultMessageBuilder implements MessageBuilder{
    @Override
    public String generate(JoinPoint joinPoint) {
        return "Executed %s".formatted(joinPoint.getSignature().getName());
    }
}
