package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.AgreementMapper;
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
import static org.mockito.Mockito.verify;

class AgreementServletTest extends TestConfigurationEnvironment {

    private AgreementServlet agreementServlet;
    private AgreementService agreementService;
    private Converter converter;

    @BeforeEach
    void setUp() {
        agreementServlet = new AgreementServlet();
        agreementService = mock(AgreementService.class);
        converter = mock(Converter.class);
        agreementServlet.inject(agreementService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfAgreementDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<AgreementDto> agreements = new ArrayList<>();
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(agreementService.getPage(new Pageable(0, 10))).thenReturn(agreements);
        when(authentication.getUser()).thenReturn(User.builder().role(Role.ADMIN).build());

        agreementServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(agreementService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedAgreement_whenAgreementValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, AgreementDto.class))
            .thenReturn(agreementDto);
        when(agreementService.create(agreementDto)).thenReturn(agreementDto);
        when(converter.convertObjectToJson(agreementDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(authentication.getUser()).thenReturn(User.builder().role(Role.ADMIN).build());

        agreementServlet.doPost(request, response);

        verify(converter).getRequestBody(request, AgreementDto.class);
        verify(agreementService).create(agreementDto);
        verify(converter).convertObjectToJson(agreementDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenAgreementNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, AgreementDto.class))
            .thenReturn(agreementDto);
        when(path.toString()).thenReturn("property name");
        when(agreementService.create(agreementDto)).thenThrow(ValidationException.class);


        assertThrows(ValidationException.class, ()->agreementServlet.doPost(request, response));

        verify(converter).getRequestBody(request, AgreementDto.class);
        verify(agreementService).create(agreementDto);
    }

}