package by.eugenekulik.e2e;


import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
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
class MetersTypeTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testGetAll_shouldReturnStatusOkAllMetersData() {
            RequestEntity<Void> request =
                RequestEntity.get("/meters-type")
                    .headers(headerUtils.withClientToken())
                    .build();

            ResponseEntity<MetersTypeResponseDto[]> response = restTemplate
                .exchange(request, MetersTypeResponseDto[].class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .hasSize(3)
                .anyMatch(metersTypeResponseDto -> metersTypeResponseDto.name().equals("warm_water"))
                .anyMatch(metersTypeResponseDto -> metersTypeResponseDto.name().equals("cold_water"))
                .anyMatch(metersTypeResponseDto -> metersTypeResponseDto.name().equals("heating"));
        }

        @Test
        void testCreationMetersType_shouldReturnStatusCreatedAndBodyWithCreatedMetersType_whenMetersTypeValid() {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric");
            RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("/meters-type")
                .headers(headerUtils.withAdminToken())
                .body(metersTypeRequestDto);

            ResponseEntity<MetersTypeResponseDto> response = restTemplate
                .exchange(request, MetersTypeResponseDto.class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertThat(response.getBody())
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "electric");
        }
    }

    @Nested
    @DisplayName("Bad request")
    class BadRequest {
        @Test
        void testCreationMetersType_shouldReturnStatusBadRequest_whenMetersTypeNotValid() {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric*43");
            RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("/meters-type")
                .headers(headerUtils.withAdminToken())
                .body(metersTypeRequestDto);

            ResponseEntity<String> response = restTemplate
                .exchange(request, String.class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Security")
    class Security{
        @Test
        void testGetPage_shouldReturnStatusForbidden_whenAnonymousUser(){

            ResponseEntity<String> response = restTemplate
                .getForEntity("/meters-type", String.class);

            assertNotNull(response);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void testCreationMetersType_shouldReturnStatusForbidden_whenUserWithRoleClient() {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric");
            RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("/meters-type")
                .headers(headerUtils.withClientToken())
                .body(metersTypeRequestDto);

            ResponseEntity<String> response = restTemplate
                .exchange(request, String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void testCreationMetersType_shouldReturnStatusForbidden_whenAnonymousUser() {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric");
            RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("/meters-type")
                .body(metersTypeRequestDto);

            ResponseEntity<String> response = restTemplate
                .exchange(request, String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }




}
