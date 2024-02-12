package by.eugenekulik.service.aspect;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.Rec;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.utils.ContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
public class AspectService {

    private Logger log = LoggerFactory.getLogger(AspectService.class);
    private RecRepository recRepository;

    @Pointcut(value = "@annotation(allowedRoles) && execution(* *(..)) ")
    public void callAllowedRolesMethod(AllowedRoles allowedRoles) {

    }

    @Pointcut(value = "@within(by.eugenekulik.service.aspect.Auditable) && execution(* *(..))")
    public void callAuditableMethod() {
    }

    @Pointcut("@within(by.eugenekulik.service.aspect.Timed) && execution(* *(..))")
    public void callTimedMethod() {
    }

    @Pointcut("@within(by.eugenekulik.service.aspect.Loggable) && execution(* *(..))")
    public void callLoggableMethod() {
    }

    @Before(value = "callAllowedRolesMethod(allowedRoles)", argNames = "allowedRoles")
    public void secure(AllowedRoles allowedRoles) {
        HttpServletRequest request = ContextUtils.getBean(HttpServletRequest.class);
        Object object = request.getSession().getAttribute("authentication");
        Authentication authentication;
        if (object == null) {
            authentication = new Authentication(User.guest);
        } else {
            authentication = (Authentication) object;
        }
        if (Arrays.stream(allowedRoles.value())
            .filter(role -> authentication.getUser().getRole().equals(role))
            .findAny()
            .isEmpty()) {
            throw new AccessDeniedException("access denied for this user");
        }
    }

    @Around("callLoggableMethod()")
    public Object log(ProceedingJoinPoint joinPoint) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("class: ").append(joinPoint.getSignature().getDeclaringType().getName());
            builder.append(", method: ").append(joinPoint.getSignature().getName());
            builder.append(", params: ");
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
            log.info("before: {}", builder);
            Object retVal = joinPoint.proceed();
            log.info("after: {}", builder);
            return retVal;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }


    @Around("callTimedMethod()")
    public Object timed(ProceedingJoinPoint joinPoint) {
        try {
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
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        if (recRepository == null) {
            recRepository = ContextUtils.getBean(RecRepository.class);
        }
        StringBuilder builder = new StringBuilder();
        HttpServletRequest httpServletRequest = ContextUtils.getBean(HttpServletRequest.class);
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
