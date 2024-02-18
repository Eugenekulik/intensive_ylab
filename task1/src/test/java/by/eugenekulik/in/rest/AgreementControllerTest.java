package by.eugenekulik.in.rest;

import by.eugenekulik.TestConfig;
import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.service.AgreementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class AgreementControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AgreementController agreementController;

    @Mock
    private AgreementService agreementService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(agreementController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void testGetPage() throws Exception {
        List<AgreementResponseDto> response = new ArrayList<>();
        when(agreementService.getPage(PageRequest.of(0,10))).thenReturn(response);
        mockMvc.perform(get("/agreement"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testCreate() throws Exception {
        AgreementRequestDto requestDto = new AgreementRequestDto(1L,1L);
        AgreementResponseDto responseDto = new AgreementResponseDto(1L, 1L, 1L);

        when(agreementService.create(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/agreement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(responseDto.id()))
            .andExpect(jsonPath("$.userId").value(responseDto.userId()));
    }



}