package by.eugenekulik.in.servlet.filter;


import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.exception.UnsupportedMediaTypeException;
import by.eugenekulik.service.aspect.Loggable;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "a_exception_handler", value = "/*")
@ApplicationScoped
public class ExseptionHandlerFilter implements Filter {

    private Converter converter;
    private List<Class<? extends Exception>> exceptions;


    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }
    /**
     * Default list of exception classes to catch.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        exceptions = new ArrayList<>();
        exceptions.add(RegistrationException.class);
        exceptions.add(AuthenticationException.class);
        exceptions.add(AccessDeniedException.class);
        exceptions.add(ValidationException.class);
        exceptions.add(UnsupportedMediaTypeException.class);
    }

    @Override
    @Loggable
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (RuntimeException exception) {
            if (exceptions.contains(exception.getClass())) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.getWriter().append(converter.convertObjectToJson(exception.getMessage()));
            } else {
                throw new RuntimeException(exception);
            }
        }
    }
}
