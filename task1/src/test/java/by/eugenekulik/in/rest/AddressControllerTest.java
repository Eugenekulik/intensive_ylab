package by.eugenekulik.in.rest;

import by.eugenekulik.TestConfig;
import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
class AddressControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(addressController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void testGetPage() throws Exception {
        List<AddressResponseDto> response = new ArrayList<>();
        when(addressService.getPage(PageRequest.of(0,10))).thenReturn(response);
        mockMvc.perform(get("/address"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

    }

    @Test
    void testCreate() throws Exception {
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

    }


}