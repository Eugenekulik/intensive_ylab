package by.eugenekulik.starter.audit.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(by.eugenekulik.starter.audit.annotation.Auditable)")
    public void callAuditableMethod() {
    }
}
