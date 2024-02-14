package by.eugenekulik.service;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.utils.ContextManager;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
@Slf4j
public class SecurityAspect {

    private static SecurityAspect INSTANCE = new SecurityAspect();
    private ContextManager contextManager;

    public static SecurityAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(ContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @Pointcut(value = "@annotation(allowedRoles) && execution(* *(..)) ")
    public void callAllowedRolesMethod(AllowedRoles allowedRoles) {
    }

    @Before(value = "callAllowedRolesMethod(allowedRoles)", argNames = "allowedRoles")
    public void secure(AllowedRoles allowedRoles) {
        HttpSession session = contextManager.getBean(HttpSession.class);
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
}
