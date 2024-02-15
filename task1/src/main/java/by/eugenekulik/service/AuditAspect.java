package by.eugenekulik.service;

import by.eugenekulik.model.Rec;
import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.utils.ContextManager;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
public class AuditAspect {

    private static AuditAspect INSTANCE = new AuditAspect();
    private ContextManager contextManager;
    private RecRepository recRepository;

    public static AuditAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(ContextManager contextManager, RecRepository recRepository) {
        this.contextManager = contextManager;
        this.recRepository = recRepository;
    }


    @Pointcut(value = "@within(by.eugenekulik.service.annotation.Auditable) && execution(* *(..))")
    public void callAuditableMethod() {
    }

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        HttpServletRequest httpServletRequest = contextManager.getBean(HttpServletRequest.class);
        Authentication authentication = (Authentication) httpServletRequest.getAttribute("authentication");
        builder.append("User: ").append(authentication.getUser().getUsername())
            .append(", with role: ").append(authentication.getUser().getRole().toString())
            .append(", path: ").append(httpServletRequest.getContextPath())
            .append(", execute: ").append(joinPoint.getSignature().getDeclaringType().getName())
            .append(".").append(joinPoint.getSignature().getName())
            .append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg).append(", "));
        builder.delete(builder.length() - 2, builder.length());
        recRepository.audit(new Rec(UUID.randomUUID().toString(), builder.toString(), LocalDateTime.now()));
    }
}
