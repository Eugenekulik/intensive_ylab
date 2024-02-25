package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.service.MetersDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = MetersDataController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MetersDataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MetersDataController metersDataController;
    @MockBean
    private MetersDataService metersDataService;

    @Test
    void testGetPage_shouldReturnPage_whenParamsNotEmpty() throws Exception {
        List<MetersDataResponseDto> response = new ArrayList<>();

        when(metersDataService.getPage(PageRequest.of(1,20))).thenReturn(response);

        mockMvc.perform(get("/meters-data").param("page", "1").param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testGetPage_shouldReturnDefaultPage_whenParamsEmpty() throws Exception {
        List<MetersDataResponseDto> response = new ArrayList<>();

        when(metersDataService.getPage(PageRequest.of(0,10))).thenReturn(response);

        mockMvc.perform(get("/meters-data"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testCreate_shouldReturnOkStatusAndBodyWithSavedMetersData_whenMetersDataValid() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0,
            LocalDateTime.of(2024, 2, 18, 0, 0));
        MetersDataRequestDto metersDataRequestDto =
            new MetersDataRequestDto(1L, 1L, 100.0);


        when(metersDataService.create(metersDataRequestDto)).thenReturn(metersDataResponseDto);

        mockMvc.perform(post("/meters-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(metersDataRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$.agreementId").value(metersDataResponseDto.agreementId()));

        verify(metersDataService).create(metersDataRequestDto);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/metersDataNotValid.csv", delimiterString = ";", numLinesToSkip = 1)
    void testCreate_shouldReturnBadRequest_whenArgumentsNotValid(Long agreementId, Long metersTypeId, Double value) throws Exception {
        MetersDataRequestDto metersDataRequestDto =
            new MetersDataRequestDto(agreementId, metersTypeId, value);

        mockMvc.perform(post("/meters-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(metersDataRequestDto)))
            .andExpect(status().isBadRequest());

        verify(metersDataService, never()).create(metersDataRequestDto);
    }

    @Test
    void testGetLast_shouldReturnLastMetersDataByTypeAndAgreement_whenArgumentsValid() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0, LocalDateTime.now());
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findLastByAgreementAndType(agreementId, type)).thenReturn(metersDataResponseDto);

        mockMvc.perform(get("/meters-data/user/last")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$.agreementId").value(metersDataResponseDto.agreementId()));
    }

    @Test
    void testGetLast_shouldReturnBabRequestStatus_whenArgumentsNotValid() throws Exception {
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findLastByAgreementAndType(agreementId, type))
            .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/meters-data/user/last")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", type))
            .andExpect(status().isBadRequest());
    }



    @Test
    void testGetUserMeters_shouldReturnDefaultPageOfMetersData_whenArgumentsValidAndPageNotSet() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0, LocalDateTime.now());
        List<MetersDataResponseDto> response = new ArrayList<>();
        response.add(metersDataResponseDto);
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findByAgreementAndType(agreementId, type, PageRequest.of(0,10)))
            .thenReturn(response);

        mockMvc.perform(get("/meters-data/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$[0].agreementId").value(metersDataResponseDto.agreementId()));
    }

    @Test
    void testGetUserMeters_shouldReturnPageOfMetersData_whenArgumentsValidAndPageSet() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0, LocalDateTime.now());
        List<MetersDataResponseDto> response = new ArrayList<>();
        response.add(metersDataResponseDto);
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findByAgreementAndType(agreementId, type, PageRequest.of(1,5)))
            .thenReturn(response);

        mockMvc.perform(get("/meters-data/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName")
                .param("page", "1")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$[0].agreementId").value(metersDataResponseDto.agreementId()));
    }

    @Test
    void testGetUserMeters_shouldReturnBadRequest_whenArgumentsNotValid() throws Exception {
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findByAgreementAndType(agreementId, type, PageRequest.of(0,10)))
            .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/meters-data/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName"))
            .andExpect(status().isBadRequest());
    }



}