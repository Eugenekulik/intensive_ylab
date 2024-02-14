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
    private ValidationService validationService;
    private AddressMapper mapper;
    private Converter converter;

    @BeforeEach
    void setUp() {
        addressServlet = new AddressServlet();
        addressService = mock(AddressService.class);
        mapper = mock(AddressMapper.class);
        validationService = mock(ValidationService.class);
        converter = mock(Converter.class);
        addressServlet.inject(addressService, mapper, validationService, converter);
    }

    @Test
    void testDoGet_shouldWriteToResponseJsonOfListOfAddressDto() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Address> addresses = new ArrayList<>();
        Address address = Address.builder()
            .id(1L)
            .region("region")
            .district("district")
            .city("city")
            .street("street")
            .house("house")
            .apartment("apartment")
            .build();
        addresses.add(address);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getInteger(request, "page")).thenReturn(0);
        when(converter.getInteger(request, "count")).thenReturn(10);
        when(mapper.fromAddress(any())).thenReturn(mock(AddressDto.class));
        when(converter.convertObjectToJson(any())).thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);
        when(addressService.getPage(new Pageable(0, 10))).thenReturn(addresses);

        addressServlet.doGet(request, response);

        verify(converter).getInteger(request, "page");
        verify(converter).getInteger(request, "count");
        verify(mapper).fromAddress(any());
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
        Address address = mock(Address.class);
        PrintWriter printWriter = mock(PrintWriter.class);

        when(converter.getRequestBody(request, AddressDto.class))
            .thenReturn(addressDto);
        when(validationService.validateObject(addressDto, "id"))
            .thenReturn(error);
        when(mapper.fromAddressDto(addressDto)).thenReturn(address);
        when(addressService.create(address)).thenReturn(address);
        when(mapper.fromAddress(address)).thenReturn(addressDto);
        when(converter.convertObjectToJson(addressDto))
            .thenReturn("json");
        when(response.getWriter()).thenReturn(printWriter);

        addressServlet.doPost(request, response);

        verify(converter).getRequestBody(request, AddressDto.class);
        verify(validationService).validateObject(addressDto, "id");
        verify(mapper).fromAddressDto(addressDto);
        verify(addressService).create(address);
        verify(mapper).fromAddress(address);
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
        when(validationService.validateObject(addressDto, "id"))
            .thenReturn(errors);
        when(error.getMessage()).thenReturn("constraint message");
        when(error.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("property name");


        assertThrows(ValidationException.class, ()->addressServlet.doPost(request, response));

        verify(converter).getRequestBody(request, AddressDto.class);
        verify(validationService).validateObject(addressDto, "id");
        verify(addressService, never()).create(any());
    }

}