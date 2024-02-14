package by.eugenekulik.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Set;

@Aspect
public class ValidationAspect {

    private static ValidationAspect INSTANCE = new ValidationAspect();
    private ValidationService validationService;

    public static ValidationAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Pointcut("execution(* *(.., @jakarta.validation.Valid (*), ..))")
    public void callValidationParam(){}


    @Before("callValidationParam()")
    public void validateMethodParameters(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && arg.getClass().isAnnotationPresent(Valid.class)) {
                Set<ConstraintViolation<Object>> errors = validationService.validateObject(arg);
                if(!errors.isEmpty()){
                    StringBuilder message = new StringBuilder();
                    message.append("errors: ");
                    errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                        .append(e.getMessage()).append(", "));
                    throw new ValidationException(message.toString());
                }
            }
        }
    }

}
