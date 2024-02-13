package by.eugenekulik.config;

import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.AspectService;
import by.eugenekulik.utils.ContextUtils;
import by.eugenekulik.utils.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    @Produces
    @Named("converter")
    public Converter converter(){
        return new Converter(new ObjectMapper().findAndRegisterModules());
    }

    @Produces
    @Named("aspectService")
    public AspectService aspectService(RecRepository recRepository, ContextUtils contextUtils,
                                       TransactionManager transactionManager){
        AspectService aspectService = AspectService.aspectOf();
        aspectService.inject(recRepository, contextUtils, transactionManager);
        return aspectService;
    }

}

