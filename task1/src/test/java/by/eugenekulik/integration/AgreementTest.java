package by.eugenekulik.integration;


import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
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

    @Nested
    @DisplayName("Positive testing")
    class Positive {
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
        void testGetPage_shouldReturnStatusOkAndBodyPageOfAgreement_whenUserWithRoleAdmin() {
            RequestEntity<Void> request = RequestEntity.get("")
                .headers(headerUtils.withAdminToken())
                .build();

            ResponseEntity<AgreementResponseDto[]> response =
                restTemplate.exchange("/agreement", HttpMethod.GET, request, AgreementResponseDto[].class);

            assertNotNull(response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .anyMatch(agreement -> agreement.id().equals(1L)
                    && agreement.active().equals(true)
                    && agreement.userId().equals(2L)
                    && agreement.addressId().equals(1L));
        }
    }

    @Nested
    @DisplayName("Bad request")
    class BadRequest{
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
    }

    @Nested
    @DisplayName("Security")
    class Security {
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

        @Test
        void testGetPage_shouldReturnStatusForbidden_whenUserWithRoleClient() {
            RequestEntity<Void> request = RequestEntity.get("")
                .headers(headerUtils.withClientToken())
                .build();

            ResponseEntity<String> response =
                restTemplate.exchange("/agreement", HttpMethod.GET, request, String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void testGetPage_shouldReturnStatusForbidden_whenAnonymousUser() {
            ResponseEntity<String> response = restTemplate.getForEntity("/agreement", String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }


    }







}
