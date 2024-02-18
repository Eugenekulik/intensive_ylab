package by.eugenekulik.in.rest;

import by.eugenekulik.TestConfig;
import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.service.MetersTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class MetersTypeControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MetersTypeController metersTypeController;

    @Mock
    private MetersTypeService metersTypeService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
            .standaloneSetup(metersTypeController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).
            build();
    }


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
    void testCreate() throws Exception {
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
    }

}