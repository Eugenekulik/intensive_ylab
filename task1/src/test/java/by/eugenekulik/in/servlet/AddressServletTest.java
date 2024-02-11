package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.logic.AddressService;
import by.eugenekulik.service.mapper.AddressMapper;
import by.eugenekulik.service.mapper.AddressMapperImpl;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AddressServletTest {

    private static AddressServlet addressServlet;
    private static AddressService addressService;
    private static ValidationService validationService;
    private static AddressMapper mapper;

    @BeforeAll
    static void setUp(){
        addressServlet = new AddressServlet();
        addressService = mock(AddressService.class);
        mapper = new AddressMapperImpl();
        validationService = mock(ValidationService.class);
        addressServlet.inject(addressService, mapper, validationService);
    }

    @Test
    void testDoGet_should() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Address> addresses = new ArrayList<>();
        PrintWriter writer = mock(PrintWriter.class);
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

        when(request.getParameter("page")).thenReturn("0");
        when(request.getParameter("count")).thenReturn("10");
        when(response.getWriter()).thenReturn(writer);
        when(addressService.getPage(new Pageable(0,10))).thenReturn(addresses);

        addressServlet.doGet(request, response);

        verify(writer).append("[{\"id\":1," +
                "\"region\":\"region\"," +
                "\"district\":\"district\"," +
                "\"city\":\"city\"," +
                "\"street\":\"street\"," +
                "\"house\":\"house\"," +
                "\"apartment\":\"apartment\"}]");

    }


    @Test
    void testDoPost() throws IOException {
        URL url = new URL("http://localhost:8080/ylab");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String validJsonData = Converter.convertObjectToJson(Address.builder()
            .id(1L)
            .region("region")
            .district("district")
            .city("city")
            .street("street")
            .house("house")
            .apartment("apartment")
            .build());

        // Act
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = validJsonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Assert
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Add your assertions based on the response content
            // assertEquals(expectedResponse, response.toString());
        } finally {
            connection.disconnect();
        }
    }

}