package by.eugenekulik.in.rest;

import by.eugenekulik.TestConfig;
import by.eugenekulik.dto.*;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.UserService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class MetersDataControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MetersDataController metersDataController;
    @Mock
    private MetersDataService metersDataService;
    @Mock
    private AgreementService agreementService;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(metersDataController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void testGetPage() throws Exception {
        List<MetersDataResponseDto> response = new ArrayList<>();
        when(metersDataService.getPage(PageRequest.of(0,10))).thenReturn(response);
        mockMvc.perform(get("/meters-data"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testCreate() throws Exception {
        AgreementResponseDto agreementResponseDto =
            new AgreementResponseDto(1L, 1L, 1L);
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0,
            LocalDateTime.of(2024, 2, 18, 0, 0));
        MetersDataRequestDto metersDataRequestDto =
            new MetersDataRequestDto(1L, 1L, 100.0);


        when(agreementService.findById(1L)).thenReturn(agreementResponseDto);
        when(userService.currentUser())
            .thenReturn(new UserDto(1L, "username", "email", "role"));
        when(metersDataService.create(metersDataRequestDto)).thenReturn(metersDataResponseDto);

        mockMvc.perform(post("/meters-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(metersDataRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$.agreementId").value(metersDataResponseDto.agreementId()));
    }

    @Test
    void testGetLast() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0, LocalDateTime.now());
        AgreementResponseDto agreementResponseDto = new AgreementResponseDto(1L, 1L, 1L);
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findLastByAgreementAndType(agreementId, type)).thenReturn(metersDataResponseDto);
        when(agreementService.findById(1L)).thenReturn(agreementResponseDto);
        when(userService.currentUser())
            .thenReturn(new UserDto(1L, "username", "email", "role"));

        mockMvc.perform(get("/user/meters-data/last")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$.agreementId").value(metersDataResponseDto.agreementId()));
    }

    @Test
    void testGetUserMeters() throws Exception {
        MetersDataResponseDto metersDataResponseDto =
            new MetersDataResponseDto(1L, 1L, 1L, 100.0, LocalDateTime.now());
        List<MetersDataResponseDto> response = new ArrayList<>();
        response.add(metersDataResponseDto);
        AgreementResponseDto agreementResponseDto = new AgreementResponseDto(1L, 1L, 1L);
        Long agreementId = 1L;
        String type = "typeName";

        when(metersDataService.findByAgreementAndType(agreementId, type, PageRequest.of(0,10)))
            .thenReturn(response);
        when(agreementService.findById(1L)).thenReturn(agreementResponseDto);
        when(userService.currentUser())
            .thenReturn(new UserDto(1L, "username", "email", "role"));

        mockMvc.perform(get("/user/meters-data")
                .contentType(MediaType.APPLICATION_JSON)
                .param("agreementId", "1")
                .param("type", "typeName"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(metersDataResponseDto.id()))
            .andExpect(jsonPath("$[0].agreementId").value(metersDataResponseDto.agreementId()));
    }



}