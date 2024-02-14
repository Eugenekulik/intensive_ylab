package by.eugenekulik.in.servlet;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MetersDataServletTest extends TestConfigurationEnvironment {

    private MetersDataServlet metersDataServlet;
    private MetersDataService metersDataService;
    private ValidationService validationService;
    private MetersDataMapper mapper;
    private Converter converter;

    @BeforeEach
    void setUp() {
        metersDataServlet = new MetersDataServlet();
        metersDataService = mock(MetersDataService.class);
        mapper = mock(MetersDataMapper.class);
        validationService = mock(ValidationService.class);
        converter = mock(Converter.class);
        metersDataServlet.inject(metersDataService, mapper, validationService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfMetersDataDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<MetersData> metersDataes = new ArrayList<>();
        MetersData metersData = MetersData.builder()
            .id(1L)
            .agreementId(1L)
            .metersTypeId(1L)
            .build();
        metersDataes.add(metersData);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(mapper.fromMetersData(any())).thenReturn(mock(MetersDataDto.class));
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(metersDataService.getPage(new Pageable(0, 10))).thenReturn(metersDataes);

        metersDataServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(mapper).fromMetersData(any());
        verify(metersDataService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedMetersData_whenMetersDataValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        Set<ConstraintViolation<MetersDataDto>> error = Collections.emptySet();
        MetersData metersData = mock(MetersData.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, MetersDataDto.class))
            .thenReturn(metersDataDto);
        when(validationService.validateObject(metersDataDto, "id"))
            .thenReturn(error);
        when(mapper.fromMetersDataDto(metersDataDto)).thenReturn(metersData);
        when(metersDataService.create(metersData)).thenReturn(metersData);
        when(mapper.fromMetersData(metersData)).thenReturn(metersDataDto);
        when(converter.convertObjectToJson(metersDataDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);

        metersDataServlet.doPost(request, response);

        verify(converter).getRequestBody(request, MetersDataDto.class);
        verify(validationService).validateObject(metersDataDto, "id");
        verify(mapper).fromMetersDataDto(metersDataDto);
        verify(metersDataService).create(metersData);
        verify(mapper).fromMetersData(metersData);
        verify(converter).convertObjectToJson(metersDataDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenMetersDataNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        ConstraintViolation<MetersDataDto> error = mock(ConstraintViolation.class);
        Set<ConstraintViolation<MetersDataDto>> errors = Set.of(error);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, MetersDataDto.class))
            .thenReturn(metersDataDto);
        when(validationService.validateObject(metersDataDto, "id"))
            .thenReturn(errors);
        when(error.getMessage()).thenReturn("constraint message");
        when(error.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("property name");


        assertThrows(ValidationException.class, ()->metersDataServlet.doPost(request, response));

        verify(converter).getRequestBody(request, MetersDataDto.class);
        verify(validationService).validateObject(metersDataDto, "id");
        verify(metersDataService, never()).create(any());
    }

}