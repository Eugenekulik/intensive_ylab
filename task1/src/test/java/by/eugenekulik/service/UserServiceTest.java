package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestConfigurationEnvironment {


    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository, userMapper, httpSession);
    }


    @Test
    void testRegister_shouldRegisterUser_whenNotExists() {
        RegistrationDto registrationDto = mock(RegistrationDto.class);
        User user = mock(User.class);
        UserDto userDto = mock(UserDto.class);

        when(userMapper.fromUser(user)).thenReturn(userDto);
        when(userMapper.fromRegistrationDto(registrationDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(userDto, userService.register(registrationDto));

        verify(userRepository).save(user);
    }

    @Test
    void testRegister_shouldThrowException_whenUsernameOrEmailExists() {
        User user = mock(User.class);
        RegistrationDto registrationDto = new RegistrationDto("username",
            "password",
            "user@mail.ru");

        when(userRepository.save(user))
            .thenThrow(DatabaseInterectionException.class);
        when(userMapper.fromRegistrationDto(registrationDto)).thenReturn(user);

        assertThrows(RegistrationException.class, () -> userService.register(registrationDto),
            "user with this username or email already exists");

        verify(userRepository).save(user);
    }



    @Test
    void testAuthorize_shouldAuthorizeUser_whenCredentialsAreCorrect() {
        String username = "testUser";
        String password = "testPassword";
        AuthDto authDto = new AuthDto(username, password);
        User user = User.builder()
                .username(username)
                    .password(password)
                        .build();
        UserDto userDto = mock(UserDto.class);

        when(userMapper.fromUser(user)).thenReturn(userDto);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertEquals(userDto, userService.authorize(authDto));

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testAuthorize_shouldThrowException_whenUserNotFound() {
        String nonExistentUsername = "nonExistentUser";
        String password = "testPassword";
        AuthDto authDto = new AuthDto(nonExistentUsername, password);

        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class,
            () -> userService.authorize(authDto),
            "The username or password you entered is incorrect");

        verify(userRepository).findByUsername(nonExistentUsername);
    }

    @Test
    void testAuthorize_shouldThrowException_whenPasswordIncorrect() {
        String username = "testUser";
        String incorrectPassword = "incorrectPassword";
        User user = mock(User.class);
        AuthDto authDto = new AuthDto(username, incorrectPassword);

        when(user.getPassword()).thenReturn("password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class,
            () -> userService.authorize(authDto),
            "The username or password you entered is incorrect");

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        User user = mock(User.class);
        UserDto userDto = mock(UserDto.class);
        List<User> userList = List.of(user);

        when(userRepository.getPage(pageable)).thenReturn(userList);
        when(userMapper.fromUser(user)).thenReturn(userDto);

        assertThat(userService.getPage(pageable))
            .hasSize(1)
                .first()
                    .isEqualTo(userDto);

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