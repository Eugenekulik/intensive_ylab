package by.eugenekulik.service;

import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TransactionAspect {
    private static TransactionAspect INSTANCE = new TransactionAspect();
    private TransactionManager transactionManager;

    public static TransactionAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Pointcut(value = "@within(by.eugenekulik.service.annotation.Transactional) && execution(* *(..))")
    public void callTransactionMethod() {
    }

    @Around("callTransactionMethod()")
    public Object transaction(ProceedingJoinPoint joinPoint) {
        return transactionManager.doInTransaction(joinPoint::proceed);
    }
}
