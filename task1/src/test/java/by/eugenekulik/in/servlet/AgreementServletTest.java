package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.logic.AgreementService;
import by.eugenekulik.service.mapper.AgreementMapper;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    private ValidationService validationService;
    private AgreementMapper mapper;
    private Converter converter;

    @BeforeEach
    void setUp() {
        agreementServlet = new AgreementServlet();
        agreementService = mock(AgreementService.class);
        mapper = mock(AgreementMapper.class);
        validationService = mock(ValidationService.class);
        converter = mock(Converter.class);
        agreementServlet.inject(agreementService, mapper, validationService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfAgreementDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Agreement> agreementes = new ArrayList<>();
        Agreement agreement = Agreement.builder()
            .id(1L)
            .userId(1L)
            .addressId(1L)
            .build();
        agreementes.add(agreement);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(mapper.fromAgreement(any())).thenReturn(mock(AgreementDto.class));
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(agreementService.getPage(new Pageable(0, 10))).thenReturn(agreementes);
        when(authentication.getUser()).thenReturn(User.builder().role(Role.ADMIN).build());

        agreementServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(mapper).fromAgreement(any());
        verify(agreementService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedAgreement_whenAgreementValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        Set<ConstraintViolation<AgreementDto>> error = Collections.emptySet();
        Agreement agreement = mock(Agreement.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, AgreementDto.class))
            .thenReturn(agreementDto);
        when(validationService.validateObject(agreementDto, "id"))
            .thenReturn(error);
        when(mapper.fromAgreementDto(agreementDto)).thenReturn(agreement);
        when(agreementService.create(agreement)).thenReturn(agreement);
        when(mapper.fromAgreement(agreement)).thenReturn(agreementDto);
        when(converter.convertObjectToJson(agreementDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(authentication.getUser()).thenReturn(User.builder().role(Role.ADMIN).build());

        agreementServlet.doPost(request, response);

        verify(converter).getRequestBody(request, AgreementDto.class);
        verify(validationService).validateObject(agreementDto, "id");
        verify(mapper).fromAgreementDto(agreementDto);
        verify(agreementService).create(agreement);
        verify(mapper).fromAgreement(agreement);
        verify(converter).convertObjectToJson(agreementDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenAgreementNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        ConstraintViolation<AgreementDto> error = mock(ConstraintViolation.class);
        Set<ConstraintViolation<AgreementDto>> errors = Set.of(error);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, AgreementDto.class))
            .thenReturn(agreementDto);
        when(validationService.validateObject(agreementDto, "id"))
            .thenReturn(errors);
        when(error.getMessage()).thenReturn("constraint message");
        when(error.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("property name");


        assertThrows(ValidationException.class, ()->agreementServlet.doPost(request, response));

        verify(converter).getRequestBody(request, AgreementDto.class);
        verify(validationService).validateObject(agreementDto, "id");
        verify(agreementService, never()).create(any());
    }

}