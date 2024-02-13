package by.eugenekulik.service.aspect;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.Rec;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.utils.ContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Slf4j
public class AspectService {

    private static AspectService INSTANCE = new AspectService();
    private RecRepository recRepository;
    private ContextUtils contextUtils;
    private TransactionManager transactionManager;

    public static AspectService aspectOf() {
        return INSTANCE;
    }

    public void inject(RecRepository recRepository, ContextUtils contextUtils,
                       TransactionManager transactionManager) {
        this.recRepository = recRepository;
        this.contextUtils = contextUtils;
        this.transactionManager = transactionManager;
    }

    @Pointcut(value = "@within(by.eugenekulik.service.aspect.Transactional) && execution(* *(..))")
    public void callTransactionMethod() {
    }

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

    @Around("callTransactionMethod()")
    public Object transaction(ProceedingJoinPoint joinPoint) {
        return transactionManager.doInTransaction(joinPoint::proceed);
    }

    @Before(value = "callAllowedRolesMethod(allowedRoles)", argNames = "allowedRoles")
    public void secure(AllowedRoles allowedRoles) {
        HttpSession session = contextUtils.getBean(HttpSession.class);
        Object object = session.getAttribute("authentication");
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
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append("class: ").append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(", method: ").append(joinPoint.getSignature().getName());
        builder.append(", params: ");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        log.info("before: {}", builder);
        Object retVal = joinPoint.proceed();
        log.info("after: {}", builder);
        return retVal;
    }


    @Around("callTimedMethod()")
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

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();
        HttpServletRequest httpServletRequest = contextUtils.getBean(HttpServletRequest.class);
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
