package by.eugenekulik.in.servlet;

import by.eugenekulik.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutServletTest {


    private Converter converter;
    private LogoutServlet logoutServlet;

    @BeforeEach
    void setUp(){
        converter = mock(Converter.class);
        logoutServlet = new LogoutServlet();
        logoutServlet.inject(converter);
    }

    @Test
    void testDoGet() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        HttpSession session = mock(HttpSession.class);

        when(response.getWriter()).thenReturn(printWriter);
        when(request.getSession()).thenReturn(session);
        when(converter.convertObjectToJson("Logout")).thenReturn("Logout");

        logoutServlet.doGet(request, response);

        verify(response).getWriter();
        verify(request).getSession();
        verify(printWriter).append("Logout");
        verify(response).setStatus(200);
        verify(session).setAttribute(eq("authentication"), any());
    }
}