package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.service.AuthenticationService;
import by.eugenekulik.tag.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationService authenticationService;


    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testSignUp_shouldReturnStatusCreatedAndBodyToken_whenArgumentsValid() throws Exception {
            RegistrationDto registrationDto =
                new RegistrationDto("username", "password", "user@mail.ru");
            JwtResponseDto jwtResponseDto = new JwtResponseDto("token");

            when(authenticationService.signUp(registrationDto)).thenReturn(jwtResponseDto);

            mockMvc.perform(post("/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(jwtResponseDto.token()));

            verify(authenticationService).signUp(registrationDto);
        }

        @Test
        void testSignIn_shouldReturnStatusOkAndToken_whenValidCredentials() throws Exception {
            AuthDto authDto = new AuthDto("username", "password");
            JwtResponseDto jwtResponseDto = new JwtResponseDto("token");

            when(authenticationService.signIn(authDto)).thenReturn(jwtResponseDto);

            mockMvc.perform(post("/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(jwtResponseDto.token()));

            verify(authenticationService).signIn(authDto);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @ParameterizedTest
        @CsvFileSource(resources = "/csv/userNotValid.csv", delimiterString = ";", numLinesToSkip = 1)
        void testSignUp_shouldReturnBadRequest_whenArgumentsNotValid(String username, String password, String email) throws Exception {
            RegistrationDto registrationDto = new RegistrationDto(username, password, email);

            mockMvc.perform(post("/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, never()).signUp(registrationDto);
        }

        @Test
        void testSignIn_shouldReturnBadRequest_whenNotValidCredentials() throws Exception {
            AuthDto authDto = new AuthDto("username", "password");

            when(authenticationService.signIn(authDto)).thenThrow(AuthenticationException.class);

            mockMvc.perform(post("/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isBadRequest());

            verify(authenticationService).signIn(authDto);
        }

        @Test
        void testSignIn_shouldReturnBadRequest_whenNotValidAuthDto() throws Exception {
            AuthDto authDto = new AuthDto("use", "password");


            mockMvc.perform(post("/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isBadRequest());

            verify(authenticationService, never()).signIn(authDto);
        }
    }
}