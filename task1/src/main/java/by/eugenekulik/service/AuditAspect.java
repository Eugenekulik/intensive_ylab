package by.eugenekulik.service;

import by.eugenekulik.model.Rec;
import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Service
@RequiredArgsConstructor
public class AuditAspect {

    private final RecRepository recRepository;




    @Pointcut(value = "@annotation(by.eugenekulik.service.annotation.Auditable)")
    public void callAuditableMethod() {
    }

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            StringBuilder builder = new StringBuilder();
            builder.append("User: ").append(authentication.getName())
                .append(", with role: ")
                .append(authentication.getAuthorities().stream().findAny().map(GrantedAuthority::toString)
                    .orElse("empty"))
                .append(", execute: ").append(joinPoint.getSignature().getDeclaringType().getName())
                .append(".").append(joinPoint.getSignature().getName())
                .append("(");
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg).append(", "));
            builder.delete(builder.length() - 2, builder.length());
            recRepository.audit(new Rec(UUID.randomUUID(), builder.toString(), LocalDateTime.now()));
        }
    }
}
