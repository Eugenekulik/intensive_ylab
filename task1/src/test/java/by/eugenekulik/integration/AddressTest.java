package by.eugenekulik.integration;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = IntegrationTestConfig.class)
class AddressTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testCreationAddress_shouldReturnStatusCreatedAndBodyWithCreatedAddress_whenAddressValid() {
            AddressRequestDto requestDto =
                new AddressRequestDto("Minsk region", "Minsk district", "Minsk",
                    "Nezavisimosti", "50", "31");
            RequestEntity<AddressRequestDto> request = RequestEntity
                .post("")
                .headers(headerUtils.withAdminToken())
                .body(requestDto);

            ResponseEntity<AddressResponseDto> response = restTemplate
                .exchange("/address", HttpMethod.POST, request, AddressResponseDto.class);


            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("region", "Minsk region")
                .hasNoNullFieldsOrProperties();
        }

        @Test
        void testGetPageOfAddresses_shouldReturnStatusOkAndBodyWithPageOfAddresses_whenUserWithRoleAdmin() {
            RequestEntity<?> request = RequestEntity
                .get("")
                .headers(headerUtils.withAdminToken())
                .build();

            ResponseEntity<AddressResponseDto[]> response =
                restTemplate.exchange("/address", HttpMethod.GET, request, AddressResponseDto[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                .anyMatch(addressResponseDto -> addressResponseDto.id().equals(1L)
                    && addressResponseDto.region().equals("Minsk region"));
        }
    }

    @Nested
    @DisplayName("Bad request")
    class BadRequest {
        @Test
        void testCreationAddress_shouldReturnStatusBadRequest_whenAddressNotValid() {
            AddressRequestDto requestDto =
                new AddressRequestDto("Minsk region", "Minsk district", "Minsk",
                    "Nezavisimosti", "50", "31*.-");
            RequestEntity<AddressRequestDto> request = RequestEntity
                .post("")
                .headers(headerUtils.withAdminToken())
                .body(requestDto);

            ResponseEntity<String> response = restTemplate
                .exchange("/address", HttpMethod.POST, request, String.class);


            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }




    }

    @Nested
    @DisplayName("Security")
    class Secured {
        @Test
        void testCreationAddress_shouldReturnStatusForbidden_whenUserWithRoleClient () {
            AddressRequestDto requestDto =
                new AddressRequestDto("Minsk region", "Minsk district", "Minsk",
                    "Nezavisimosti", "50", "31");
            RequestEntity<AddressRequestDto> request = RequestEntity
                .post("")
                .headers(headerUtils.withClientToken())
                .body(requestDto);

            ResponseEntity<String> response = restTemplate
                .exchange("/address", HttpMethod.POST, request, String.class);


            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void testCreationAddress_shouldReturnStatusForbidden_whenAnonymousUser () {
            AddressRequestDto requestDto =
                new AddressRequestDto("Minsk region", "Minsk district", "Minsk",
                    "Nezavisimosti", "50", "31");
            RequestEntity<AddressRequestDto> request = RequestEntity
                .post("")
                .body(requestDto);

            ResponseEntity<String> response = restTemplate
                .exchange("/address", HttpMethod.POST, request, String.class);


            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }
}
