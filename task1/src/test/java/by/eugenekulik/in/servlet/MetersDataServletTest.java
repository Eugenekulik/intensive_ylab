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
    private Converter converter;

    @BeforeEach
    void setUp() {
        metersDataServlet = new MetersDataServlet();
        metersDataService = mock(MetersDataService.class);
        converter = mock(Converter.class);
        metersDataServlet.inject(metersDataService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfMetersDataDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<MetersDataDto> metersData = new ArrayList<>();
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(metersDataService.getPage(new Pageable(0, 10))).thenReturn(metersData);

        metersDataServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(metersDataService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedMetersData_whenMetersDataValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, MetersDataDto.class))
            .thenReturn(metersDataDto);
        when(metersDataService.create(metersDataDto)).thenReturn(metersDataDto);
        when(converter.convertObjectToJson(metersDataDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);

        metersDataServlet.doPost(request, response);

        verify(converter).getRequestBody(request, MetersDataDto.class);
        verify(metersDataService).create(metersDataDto);
        verify(converter).convertObjectToJson(metersDataDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenMetersDataNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, MetersDataDto.class))
            .thenReturn(metersDataDto);
        when(path.toString()).thenReturn("property name");
        when(metersDataService.create(metersDataDto))
            .thenThrow(ValidationException.class);


        assertThrows(ValidationException.class, ()->metersDataServlet.doPost(request, response));

        verify(converter).getRequestBody(request, MetersDataDto.class);
        verify(metersDataService).create(metersDataDto);
    }

}