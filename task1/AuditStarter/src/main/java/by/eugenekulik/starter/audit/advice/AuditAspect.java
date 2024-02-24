package by.eugenekulik.starter.audit.advice;

import by.eugenekulik.starter.audit.model.AuditRecord;
import by.eugenekulik.starter.audit.service.MessageBuilder;
import by.eugenekulik.starter.audit.service.RecordStorage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
public class AuditAspect {

    private final MessageBuilder messageBuilder;
    private final RecordStorage recordStorage;


    public AuditAspect(MessageBuilder messageBuilder, RecordStorage recordStorage) {
        this.messageBuilder = messageBuilder;
        this.recordStorage = recordStorage;
    }

    @AfterReturning("by.eugenekulik.starter.audit.pointcut.Pointcuts.callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setId(UUID.randomUUID());
        auditRecord.setTime(LocalDateTime.now());
        auditRecord.setMessage(messageBuilder.generate(joinPoint));
        recordStorage.save(auditRecord);
    }


}
