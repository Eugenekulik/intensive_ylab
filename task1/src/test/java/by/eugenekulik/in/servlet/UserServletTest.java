package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.logic.UserService;
import by.eugenekulik.service.mapper.UserMapper;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServletTest {
    private UserServlet userServlet;
    private UserService userService;
    private UserMapper mapper;
    private Converter converter;

    @BeforeEach
    void setUp() {
        userServlet = new UserServlet();
        userService = mock(UserService.class);
        mapper = mock(UserMapper.class);
        converter = mock(Converter.class);
        userServlet.inject(userService, mapper, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfUserDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<User> users = new ArrayList<>();
        User user = mock(User.class);
        users.add(user);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(mapper.fromUser(any())).thenReturn(mock(UserDto.class));
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(userService.getPage(new Pageable(0, 10))).thenReturn(users);

        userServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(mapper).fromUser(any());
        verify(userService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }
}