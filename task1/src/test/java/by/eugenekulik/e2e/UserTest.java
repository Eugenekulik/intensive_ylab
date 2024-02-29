package by.eugenekulik.e2e;

import by.eugenekulik.dto.UserDto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@E2ETest
class UserTest {

    @Autowired
    private HeaderUtils headerUtils;
    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive{
        @Test
        void testGetPageOfUsers_shouldReturnStatusOkAndBodyArrayOfUsers() {
            int page = 0;
            int size = 3;
            RequestEntity<Void> request = RequestEntity
                .get(UriComponentsBuilder
                    .fromUri(URI.create("/user"))
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build().toUri())
                .headers(headerUtils.withAdminToken())
                .build();
            ResponseEntity<UserDto[]> response =
                restTemplate.exchange(request, UserDto[].class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .hasSize(3)
                .anyMatch(userDto -> userDto.id().equals(1L) && userDto.username().equals("admin"))
                .anyMatch(userDto -> userDto.id().equals(2L) && userDto.username().equals("user1"))
                .anyMatch(userDto -> userDto.id().equals(3L) && userDto.username().equals("user2"));
        }

    }

    @Nested
    @DisplayName("Security")
    class Security {
        @Test
        void testGetPageOfUser_shouldReturnForbidden_whenUserWithRoleClient(){
            int page = 0;
            int size = 3;
            RequestEntity<?> request = RequestEntity.get("/user")
                .headers(headerUtils.withClientToken())
                .build();

            ResponseEntity<String> response =
                restTemplate.exchange(request, String.class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void testGetPageOfUser_shouldReturnForbidden_whenAnonymousUser(){
            ResponseEntity<String> response =
                restTemplate.getForEntity("/user", String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }




}
