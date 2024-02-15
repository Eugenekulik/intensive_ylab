package by.eugenekulik;

import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.*;
import by.eugenekulik.utils.ContextManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Validation;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
public class TestConfigurationEnvironment {
    protected static PostgresTestContainer postgreSQLContainer;
    protected static ContextManager contextManager;
    protected static TransactionManager transactionManager;
    protected static Authentication authentication;
    protected static HttpSession httpSession;


    //Mock aspects
    static {
        transactionManager = mock(TransactionManager.class);
        contextManager = mock(ContextManager.class);
        SecurityAspect.aspectOf().inject(contextManager);
        TransactionAspect.aspectOf().inject(transactionManager);
        AuditAspect.aspectOf().inject(contextManager, mock(RecRepository.class));

        httpSession = mock(HttpSession.class);
        authentication = mock(Authentication.class);
        when(contextManager.getBean(HttpSession.class)).thenReturn(httpSession);
        when(httpSession.getAttribute("authentication")).thenReturn(authentication);
    }


}
