package by.eugenekulik.integration;

import by.eugenekulik.dto.MetersDataResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class MetersDataTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testGetPage_shouldReturnStatusOkAndBodyWithPageOfMetersData_whenUserWithRoleAdmin(){
            RequestEntity<Void> request = RequestEntity.get("", Map.of("page", 0, "size", 10))
                .headers(headerUtils.withAdminToken())
                .build();

            ResponseEntity<MetersDataResponseDto[]> response = restTemplate
                .exchange("/meters-data", HttpMethod.GET, request, MetersDataResponseDto[].class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .hasSize(1)
                .anyMatch(md -> md.id().equals(1L) && md.metersTypeId().equals(1L));
        }
    }
}
