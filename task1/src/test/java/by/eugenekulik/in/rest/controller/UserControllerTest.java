package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;


    @Test
    void testGetPage_shouldReturnPage_whenParamsNotEmpty() throws Exception {
        List<UserDto> response = new ArrayList<>();

        when(userService.getPage(PageRequest.of(1,20))).thenReturn(response);

        mockMvc.perform(get("/user").param("page", "1").param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testGetPage_shouldReturnDefaultPage_whenParamsEmpty() throws Exception {
        List<UserDto> response = new ArrayList<>();

        when(userService.getPage(PageRequest.of(0,10))).thenReturn(response);

        mockMvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }



}