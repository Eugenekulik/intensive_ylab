package by.eugenekulik.starter.audit.service;

import org.aspectj.lang.JoinPoint;

@FunctionalInterface
public interface MessageBuilder {

    String generate(JoinPoint joinPoint);

}
