package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.service.MetersTypeService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MetersTypeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
class MetersTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MetersTypeController metersTypeController;
    @MockBean
    private MetersTypeService metersTypeService;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testGetAll() throws Exception {
            List<MetersTypeResponseDto> response = new ArrayList<>();
            when(metersTypeService.findAll()).thenReturn(response);
            mockMvc.perform(get("/meters-type"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        }

        @Test
        void testCreate_shouldReturnStatusCreatedAndBodyCreatedMetersType_whenMetersTypeValid() throws Exception {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto("name");
            MetersTypeResponseDto metersTypeResponseDto = new MetersTypeResponseDto(1L, "name");

            when(metersTypeService.create(metersTypeRequestDto)).thenReturn(metersTypeResponseDto);

            mockMvc.perform(post("/meters-type")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"name\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"));

            verify(metersTypeService).create(metersTypeRequestDto);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @ParameterizedTest
        @CsvFileSource(resources = "/csv/metersTypeNotValid.csv", delimiterString = ";", numLinesToSkip = 1)
        void testCreate_shouldReturnBadRequest_whenNameNotValid(String name) throws Exception {
            MetersTypeRequestDto metersTypeRequestDto = new MetersTypeRequestDto(name);

            mockMvc.perform(post("/meters-type")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(metersTypeRequestDto)))
                .andExpect(status().isBadRequest());

            verify(metersTypeService, never()).create(metersTypeRequestDto);
        }
    }
}