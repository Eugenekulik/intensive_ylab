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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter(filterName = "a_exception_handler", value = "/*")
@ApplicationScoped
@Slf4j
public class ExseptionHandlerFilter implements Filter {

    private Converter converter;
    private Map<Class<? extends Exception>, Integer> exceptions;


    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }
    /**
     * Default list of exception classes to catch.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        exceptions = new HashMap<>();
        exceptions.put(RegistrationException.class, 400);
        exceptions.put(AuthenticationException.class, 400);
        exceptions.put(AccessDeniedException.class, 403);
        exceptions.put(ValidationException.class, 400);
        exceptions.put(UnsupportedMediaTypeException.class, 415);
    }

    @Override
    @Loggable
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (RuntimeException exception) {
            if (exceptions.containsKey(exception.getClass())) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(exceptions.get(exception.getClass()));
                response.getWriter().append(converter.convertObjectToJson(exception.getMessage()));
            } else {
                log.info(exception.getMessage(), exception);
                throw new RuntimeException(exception);
            }
        }
    }
}
