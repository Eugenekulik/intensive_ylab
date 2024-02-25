package by.eugenekulik.integration;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = IntegrationTestConfig.class)
class AddressTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testCreationAddress() {
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
    void testGetPageOfAddresses() {
        RequestEntity<?> request = RequestEntity
            .get("", Map.of("page", "0", "size", "10"))
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
