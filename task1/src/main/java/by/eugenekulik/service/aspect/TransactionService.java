package by.eugenekulik.service.aspect;

import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.utils.ContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TransactionService {


    private TransactionManager transactionManager;

    @Pointcut(value = "@within(by.eugenekulik.service.aspect.Transactional) && execution(* *(..))")
    public void callTransactionMethod() {
    }

    @Around("callTransactionMethid()")
    public Object transaction(ProceedingJoinPoint joinPoint) {
        if (transactionManager == null) {
            transactionManager = ContextUtils.getBean(TransactionManager.class);
        }
        return transactionManager.doInTransaction(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }


}
