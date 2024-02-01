package by.eugenekulik.service;

import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }


    @Test
    void testRegister_shouldRegisterUser_whenNotExists() {
        User user = mock(User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.register(user));

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testRegister_shouldThrowException_whenUsernameExists() {
        User user = mock(User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(RegistrationException.class, () -> userService.register(user),
            "A user with the same name already exists.");

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository, never()).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_shouldThrowException_whenEmailExists() {
        User user = mock(User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(RegistrationException.class, ()->userService.register(user),
            "A user with this email is already registered.");

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
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
        int page = 1;
        int count = 10;
        List<User> user = mock(List.class);

        when(userRepository.getPage(page, count)).thenReturn(user);

        assertEquals(user, userService.getPage(page, count));

        verify(userRepository).getPage(page, count);
    }

    @Test
    void testGetPage_shouldThrowException_whenPageIsNegative() {
        int page = -1;
        int count = 10;

        assertThrows(IllegalArgumentException.class,
            () -> userService.getPage(page, count),
            "page must not be negative");

        verify(userRepository, never()).getPage(anyInt(), anyInt());
    }

    @Test
    void testGetPage_shouldThrowException_whenCountIsNotPositive() {
        int page = 1;
        int count = 0;

        assertThrows(IllegalArgumentException.class, () -> userService.getPage(page, count),
            "count must be positive");

        verify(userRepository, never()).getPage(anyInt(), anyInt());
    }

}