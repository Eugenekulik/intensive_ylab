package by.eugenekulik.in.servlet;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.AddressService;
import by.eugenekulik.service.AddressMapper;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AddressServletTest extends TestConfigurationEnvironment {

    private AddressServlet addressServlet;
    private AddressService addressService;
    private Converter converter;

    @BeforeEach
    void setUp() {
        addressServlet = new AddressServlet();
        addressService = mock(AddressService.class);
        converter = mock(Converter.class);
        addressServlet.inject(addressService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfAddressDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<AddressDto> addresses = new ArrayList<>();
        AddressDto addressDto = new AddressDto(
            1L,
            "region",
            "district",
            "city",
            "street",
            "house",
            "apartment"
            );
        addresses.add(addressDto);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(addressService.getPage(new Pageable(0, 10))).thenReturn(addresses);

        addressServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(addressService).getPage(new Pageable(0, 10));
        verify(converter).convertObjectToJson(any());
        verify(printWriter).append("json");
    }


    @Test
    void testDoPost_shouldSaveAndWriteJsonOfSavedAddress_whenAddressValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AddressDto addressDto = mock(AddressDto.class);
        Set<ConstraintViolation<AddressDto>> error = Collections.emptySet();
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, AddressDto.class))
            .thenReturn(addressDto);
        when(addressService.create(addressDto)).thenReturn(addressDto);
        when(converter.convertObjectToJson(addressDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);

        addressServlet.doPost(request, response);

        verify(converter).getRequestBody(request, AddressDto.class);
        verify(addressService).create(addressDto);
        verify(converter).convertObjectToJson(addressDto);
        verify(printWriter).append("json");
    }

    @Test
    void testDoPost_shouldThrowValidationException_whenAddressNotValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AddressDto addressDto = mock(AddressDto.class);
        ConstraintViolation<AddressDto> error = mock(ConstraintViolation.class);
        Set<ConstraintViolation<AddressDto>> errors = Set.of(error);
        Path path = mock(Path.class);

        when(converter.getRequestBody(request, AddressDto.class))
            .thenReturn(addressDto);
        when(error.getMessage()).thenReturn("constraint message");
        when(error.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("property name");
        when(addressService.create(addressDto))
            .thenThrow(ValidationException.class);

        assertThrows(ValidationException.class, ()->addressServlet.doPost(request, response));

        verify(converter).getRequestBody(request, AddressDto.class);
        verify(addressService).create(addressDto);
    }

}