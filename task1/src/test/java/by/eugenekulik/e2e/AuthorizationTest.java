package by.eugenekulik.e2e;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.tag.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@E2ETest
class AuthorizationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testAuthorization(){
            AuthDto authDto = new AuthDto("admin", "password");

            ResponseEntity<JwtResponseDto> response = restTemplate.exchange(
                RequestEntity.post("/sign-in").body(authDto), JwtResponseDto.class);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().token());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void testRegistration(){
            RegistrationDto registrationDto =
                new RegistrationDto("newUser", "password", "newUser@mail.ru");

            ResponseEntity<JwtResponseDto> response = restTemplate.exchange(
                RequestEntity.post("/sign-up").body(registrationDto), JwtResponseDto.class);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().token());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }
}
