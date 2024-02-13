package by.eugenekulik;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.jdbc.repository.RecRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.aspect.AspectService;
import by.eugenekulik.utils.ContextUtils;
import jakarta.servlet.http.HttpSession;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
public class TestConfigurationEnvironment {
    protected static PostgresTestContainer postgreSQLContainer;
    protected static ContextUtils contextUtils;
    protected static TransactionManager transactionManager;
    protected static Authentication authentication;

    //Common configuration

    static {
        AspectService aspectService = AspectService.aspectOf();
        RecRepository recRepository = mock(RecRepository.class);
        transactionManager = mock(TransactionManager.class);
        contextUtils = mock(ContextUtils.class);
        aspectService.inject(recRepository, contextUtils, transactionManager);

        doNothing().when(recRepository).audit(any());
    }

    //Mock AllowedRoles aspect
    static {
        HttpSession httpSession = mock(HttpSession.class);
        authentication = mock(Authentication.class);

        when(contextUtils.getBean(HttpSession.class)).thenReturn(httpSession);
        when(httpSession.getAttribute("authentication")).thenReturn(authentication);
    }


}
