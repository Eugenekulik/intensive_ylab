package by.eugenekulik.integration;


import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = IntegrationTestConfig.class)
class AgreementTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HeaderUtils headerUtils;


    @Test
    void testCreationAgreement() {
        AgreementRequestDto requestDto =
            new AgreementRequestDto(3L, 2L);

        RequestEntity<AgreementRequestDto> request = RequestEntity
            .post("")
            .headers(headerUtils.withAdminToken())
            .body(requestDto);

        ResponseEntity<AgreementResponseDto> response =
            restTemplate.exchange("/agreement", HttpMethod.POST, request, AgreementResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody())
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("userId", 3L)
            .hasFieldOrPropertyWithValue("addressId", 2L);

    }

    @Test
    void testCreationAgreement_shouldReturnBadRequest_whenUserNotExist(){
        AgreementRequestDto requestDto =
            new AgreementRequestDto(5L, 1L);

        RequestEntity<AgreementRequestDto> request = RequestEntity
            .post("")
            .headers(headerUtils.withAdminToken())
            .body(requestDto);

        ResponseEntity<String> response =
            restTemplate.exchange("/agreement", HttpMethod.POST, request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreationAgreement_shouldReturnBadRequest_whenAddressNotExist(){
        AgreementRequestDto requestDto =
            new AgreementRequestDto(2L,    5L);

        RequestEntity<AgreementRequestDto> request = RequestEntity
            .post("")
            .headers(headerUtils.withAdminToken())
            .body(requestDto);

        ResponseEntity<String> response =
            restTemplate.exchange("/agreement", HttpMethod.POST, request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testCreationAgreement_shouldReturnForbidden_whenUserWithRoleClient(){
        AgreementRequestDto requestDto =
            new AgreementRequestDto(3L,    2L);

        RequestEntity<AgreementRequestDto> request = RequestEntity
            .post("")
            .headers(headerUtils.withClientToken())
            .body(requestDto);

        ResponseEntity<String> response =
            restTemplate.exchange("/agreement", HttpMethod.POST, request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testCreationAgreement_shouldReturnForbidden_whenAnonymousUser(){
        AgreementRequestDto requestDto =
            new AgreementRequestDto(3L,    2L);

        RequestEntity<AgreementRequestDto> request = RequestEntity
            .post("")
            .body(requestDto);

        ResponseEntity<String> response =
            restTemplate.exchange("/agreement", HttpMethod.POST, request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }



}
