package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.service.AgreementService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AgreementController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgreementController agreementController;

    @MockBean
    private AgreementService agreementService;



    @Test
    void testGetPage_shouldReturnPage_whenParamsNotEmpty() throws Exception {
        List<AgreementResponseDto> response = new ArrayList<>();

        when(agreementService.getPage(PageRequest.of(1,20))).thenReturn(response);

        mockMvc.perform(get("/agreement").param("page", "1").param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testGetPage_shouldReturnDefaultPage_whenParamsEmpty() throws Exception {
        List<AgreementResponseDto> response = new ArrayList<>();

        when(agreementService.getPage(PageRequest.of(0,10))).thenReturn(response);

        mockMvc.perform(get("/agreement"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testCreate_shouldReturnResponseWithStatusCreatedAndBodySavedAgreement_whenAgreementValid() throws Exception {
        AgreementRequestDto requestDto = new AgreementRequestDto(1L,1L);
        AgreementResponseDto responseDto = new AgreementResponseDto(1L, 1L, 1L, true);

        when(agreementService.create(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/agreement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(responseDto.id()))
            .andExpect(jsonPath("$.userId").value(responseDto.userId()));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/agreementNotValid.csv", delimiterString = ";", numLinesToSkip = 1)
    void testCreate_shouldReturnResponseWithStatusBadRequest_whenAgreementNotValid(Long userId, Long addressId) throws Exception {
        AgreementRequestDto requestDto = new AgreementRequestDto(userId, addressId);

        mockMvc.perform(post("/agreement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(status().isBadRequest());

        verify(agreementService, never()).create(requestDto);
    }


}