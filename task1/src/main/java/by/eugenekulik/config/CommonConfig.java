package by.eugenekulik.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonConfig {



    @Bean
    public Validator validator(){
        return Validation.buildDefaultValidatorFactory().getValidator();
    }




}

