package by.eugenekulik.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.validation.Validation;
import jakarta.validation.Validator;


@ApplicationScoped
public class CommonConfig {


    @Produces
    @Named("validator")
    public Validator getValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }


}

