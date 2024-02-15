package by.eugenekulik.in.servlet;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.service.MetersTypeMapper;
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

class MetersTypeServletTest extends TestConfigurationEnvironment {
    private MetersTypeServlet metersTypeServlet;
    private MetersTypeService metersTypeService;
    private Converter converter;

    @BeforeEach
    void setUp() {
        metersTypeServlet = new MetersTypeServlet();
        metersTypeService = mock(MetersTypeService.class);
        converter = mock(Converter.class);
        metersTypeServlet.inject(metersTypeService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfMetersTypeDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<MetersTypeDto> metersTypees = new ArrayList<>();
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);
        metersTypees.add(metersTypeDto);
        PrintWriter printWriter = mock(PrintWriter.class);


        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(metersTypeService.findAll()).thenReturn(metersTypees);

        metersTypeServlet.doGet(request, response);

        verify(metersTypeService).findAll();
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedMetersType_whenMetersTypeValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, MetersTypeDto.class))
            .thenReturn(metersTypeDto);
        when(metersTypeService.create(metersTypeDto)).thenReturn(metersTypeDto);
        when(converter.convertObjectToJson(metersTypeDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);

        metersTypeServlet.doPost(request, response);

        verify(converter).getRequestBody(request, MetersTypeDto.class);
        verify(metersTypeService).create(metersTypeDto);
        verify(converter).convertObjectToJson(metersTypeDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenMetersTypeNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, MetersTypeDto.class))
            .thenReturn(metersTypeDto);
        when(path.toString()).thenReturn("property name");
        when(metersTypeService.create(metersTypeDto))
            .thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, ()->metersTypeServlet.doPost(request, response));

        verify(converter).getRequestBody(request, MetersTypeDto.class);
        verify(metersTypeService).create(metersTypeDto);
    }
}