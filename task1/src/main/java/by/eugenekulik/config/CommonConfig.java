package by.eugenekulik.config;

import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.*;
import by.eugenekulik.utils.ContextManager;
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
    @Named("securityAspect")
    public SecurityAspect securityAspect(ContextManager contextManager){
        SecurityAspect securityAspect = SecurityAspect.aspectOf();
        securityAspect.inject(contextManager);
        return securityAspect;
    }

    @Produces
    @Named("validationAspect")
    public ValidationAspect validationAspect(ValidationService validationService){
        ValidationAspect validationAspect = ValidationAspect.aspectOf();
        validationAspect.inject(validationService);
        return validationAspect;
    }
    @Produces
    @Named("auditAspect")
    public AuditAspect auditAspect(ContextManager contextManager, RecRepository recRepository){
        AuditAspect auditAspect = AuditAspect.aspectOf();
        auditAspect.inject(contextManager, recRepository);
        return auditAspect;
    }
    @Produces
    @Named("transactionAspect")
    public TransactionAspect transactionAspect(TransactionManager transactionManager){
        TransactionAspect transactionAspect = TransactionAspect.aspectOf();
        transactionAspect.inject(transactionManager);
        return transactionAspect;
    }


}

