package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.service.AddressService;
import by.eugenekulik.tag.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AddressController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
class AddressControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressController addressController;

    @MockBean
    private AddressService addressService;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testGetPage_shouldReturnPage_whenParamsNotEmpty() throws Exception {
            List<AddressResponseDto> response = new ArrayList<>();

            when(addressService.getPage(PageRequest.of(1, 20))).thenReturn(response);

            mockMvc.perform(get("/address").param("page", "1").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        }

        @Test
        void testGetPage_shouldReturnDefaultPage_whenParamsEmpty() throws Exception {
            List<AddressResponseDto> response = new ArrayList<>();

            when(addressService.getPage(PageRequest.of(0, 10))).thenReturn(response);

            mockMvc.perform(get("/address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        }

        @Test
        void testCreate_shouldReturnSavedAddress_whenAddressValid() throws Exception {
            AddressRequestDto requestDto = new AddressRequestDto("region",
                "district",
                "city",
                "street",
                "house",
                "apartment");
            AddressResponseDto responseDto = new AddressResponseDto(1L, "region",
                "district",
                "city",
                "street",
                "house",
                "apartment");

            when(addressService.create(requestDto)).thenReturn(responseDto);

            mockMvc.perform(post("/address")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.region").value(responseDto.region()));

            verify(addressService).create(requestDto);
        }

    }


    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/addressNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        void testCreate_shouldReturnBadRequestStatus_whenAddressNotValid(String region,
                                                                         String district,
                                                                         String city,
                                                                         String street,
                                                                         String house,
                                                                         String apartment) throws Exception {
            AddressRequestDto requestDto =
                new AddressRequestDto(region, district, city, street, house, apartment);


            mockMvc.perform(post("/address")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

            verify(addressService, never()).create(requestDto);
        }
    }

}