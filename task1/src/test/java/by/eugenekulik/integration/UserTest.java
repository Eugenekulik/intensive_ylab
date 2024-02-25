package by.eugenekulik.integration;

import by.eugenekulik.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = IntegrationTestConfig.class)
class UserTest {

    @Autowired
    private HeaderUtils headerUtils;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testGetPageOfUsers_shouldReturnStatusOkAndBodyArrayOfUsers() {
        int page = 0;
        int size = 3;
        RequestEntity<?> request = RequestEntity.get("")
                .headers(headerUtils.withAdminToken())
                    .build();

        ResponseEntity<UserDto[]> response =
            restTemplate.exchange("/user", HttpMethod.GET, request, UserDto[].class,
                Map.of("page", page, "size", size));

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
            .hasSize(3)
            .anyMatch(userDto -> userDto.id().equals(1L) && userDto.username().equals("admin"))
            .anyMatch(userDto -> userDto.id().equals(2L) && userDto.username().equals("user1"))
            .anyMatch(userDto -> userDto.id().equals(3L) && userDto.username().equals("user2"));
    }

    @Test
    void testGetPageOfUser_shouldReturnForbidden_whenUserWithRoleClient(){
        int page = 0;
        int size = 3;
        RequestEntity<?> request = RequestEntity.get("")
            .headers(headerUtils.withClientToken())
            .build();

        ResponseEntity<String> response =
            restTemplate.exchange("/user", HttpMethod.GET, request, String.class,
                Map.of("page", page, "size", size));

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}
