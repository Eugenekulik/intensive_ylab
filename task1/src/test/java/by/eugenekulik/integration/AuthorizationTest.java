package by.eugenekulik.integration;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = IntegrationTestConfig.class)
class AuthorizationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAuthorization(){
        AuthDto authDto = new AuthDto("admin", "password");

        ResponseEntity<JwtResponseDto> response = restTemplate.exchange("/sign-in", HttpMethod.POST,
            RequestEntity.post("").body(authDto), JwtResponseDto.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().token());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegistration(){
        RegistrationDto registrationDto =
            new RegistrationDto("newUser", "password", "newUser@mail.ru");

        ResponseEntity<JwtResponseDto> response = restTemplate.exchange("/sign-up", HttpMethod.POST,
            RequestEntity.post("").body(registrationDto), JwtResponseDto.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().token());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }




}
