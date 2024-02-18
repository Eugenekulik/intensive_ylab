package by.eugenekulik.service;

import by.eugenekulik.TestConfig;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
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



}