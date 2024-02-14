package by.eugenekulik.service;

import by.eugenekulik.service.annotation.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.*;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor
public class ValidationService {


    private Validator validator;

    @Inject
    public ValidationService(Validator validator) {
        this.validator = validator;
    }


    @Loggable
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> tClass, String property, Object value) {
        Set<ConstraintViolation<T>> violations = validator.validateValue(tClass, property, value);
        return violations;
    }

    @Loggable
    public <T> Set<ConstraintViolation<T>> validateObject(T t, String... excludeProperties) {
        Set<String> propertiesToExclude = new HashSet<>(List.of(excludeProperties));
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        return constraintViolations.stream()
            .filter(c -> !propertiesToExclude.contains(c.getPropertyPath().toString()))
            .collect(Collectors.toSet());
    }


}
