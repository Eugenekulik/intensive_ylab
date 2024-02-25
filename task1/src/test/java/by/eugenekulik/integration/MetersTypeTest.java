package by.eugenekulik.integration;


import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
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
class MetersTypeTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testGetAll_shouldReturnStatusOkAllMetersData() {
        RequestEntity<Void> request =
            RequestEntity.get("",Map.of("page", 0, "size", 3))
            .headers(headerUtils.withClientToken())
            .build();

        ResponseEntity<MetersTypeResponseDto[]> response = restTemplate
            .exchange("/meters-type",HttpMethod.GET, request,  MetersTypeResponseDto[].class);

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
        RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("")
            .headers(headerUtils.withAdminToken())
            .body(metersTypeRequestDto);

        ResponseEntity<MetersTypeResponseDto> response = restTemplate
            .exchange("/meters-type", HttpMethod.POST, request, MetersTypeResponseDto.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody())
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("name", "electric");
    }

    @Test
    void testCreationMetersType_shouldReturnStatusBadRequest_whenMetersTypeNotValid() {
        MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric*43");
        RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("")
            .headers(headerUtils.withAdminToken())
            .body(metersTypeRequestDto);

        ResponseEntity<String> response = restTemplate
            .exchange("/meters-type", HttpMethod.POST, request, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreationMetersType_shouldReturnStatusForbidden_whenUserWithRoleClient() {
        MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric");
        RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("")
            .headers(headerUtils.withClientToken())
            .body(metersTypeRequestDto);

        ResponseEntity<String> response = restTemplate
            .exchange("/meters-type", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testCreationMetersType_shouldReturnStatusForbidden_whenAnonymousUser() {
        MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("electric");
        RequestEntity<MetersTypeRequestDto> request = RequestEntity.post("")
            .body(metersTypeRequestDto);

        ResponseEntity<String> response = restTemplate
            .exchange("/meters-type", HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}
