package by.eugenekulik.service;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.logic.UserService;
import by.eugenekulik.service.logic.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    private UserService userService;
    private UserRepository userRepository;
    private TransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        transactionManager = mock(TransactionManager.class);
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, transactionManager);
    }


    @Test
    void testRegister_shouldRegisterUser_whenNotExists() {
        User user = mock(User.class);

        when(transactionManager.doInTransaction(any())).thenReturn(user);

        assertEquals(user, userService.register(user));
    }

    @Test
    void testRegister_shouldThrowException_whenUsernameOrEmailExists() {
        User user = mock(User.class);

        when(transactionManager.doInTransaction(any())).thenThrow(DatabaseInterectionException.class);

        assertThrows(RegistrationException.class, () -> userService.register(user),
            "user with this username or email already exists");

        verify(transactionManager).doInTransaction(any());
    }



    @Test
    void testAuthorize_shouldAuthorizeUser_whenCredentialsAreCorrect() {
        String username = "testUser";
        String password = "testPassword";
        User user = User.builder()
                .username(username)
                    .password(password)
                        .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertEquals(user, userService.authorize(username, password));

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testAuthorize_shouldThrowException_whenUserNotFound() {
        String nonExistentUsername = "nonExistentUser";
        String password = "testPassword";

        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class,
            () -> userService.authorize(nonExistentUsername, password),
            "The username or password you entered is incorrect");

        verify(userRepository).findByUsername(nonExistentUsername);
    }

    @Test
    void testAuthorize_shouldThrowException_whenPasswordIncorrect() {
        String username = "testUser";
        String incorrectPassword = "incorrectPassword";
        User user = mock(User.class);

        when(user.getPassword()).thenReturn("password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class,
            () -> userService.authorize(username, incorrectPassword),
            "The username or password you entered is incorrect");

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        List<User> user = mock(List.class);

        when(userRepository.getPage(pageable)).thenReturn(user);

        assertEquals(user, userService.getPage(pageable));

        verify(userRepository).getPage(pageable);
    }

    @Test
    void testGetPage_shouldThrowException_whenNotValidPageable() {
        Pageable pageable = mock(Pageable.class);

        when(userRepository.getPage(pageable)).thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class,
            () -> userService.getPage(pageable));

        verify(userRepository).getPage(pageable);
    }


}