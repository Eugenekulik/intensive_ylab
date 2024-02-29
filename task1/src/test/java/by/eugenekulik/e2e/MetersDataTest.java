package by.eugenekulik.e2e;

import by.eugenekulik.dto.MetersDataResponseDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@E2ETest
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
            RequestEntity<Void> request = RequestEntity.get("/meters-data")
                .headers(headerUtils.withAdminToken())
                .build();

            ResponseEntity<MetersDataResponseDto[]> response = restTemplate
                .exchange(request, MetersDataResponseDto[].class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .hasSize(1)
                .anyMatch(md -> md.id().equals(1L) && md.metersTypeId().equals(1L));
        }
    }
}
