package by.eugenekulik.in.servlet.filter;


import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.exception.UnsupportedMediaTypeException;
import by.eugenekulik.service.annotation.Loggable;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * {@code ExceptionHandlerFilter} is a servlet filter responsible for handling exceptions
 * thrown during the processing of requests. It catches specified runtime exceptions and
 * customizes the HTTP response accordingly.
 *
 * <p>The filter is defined with the following annotations:
 * {@code @WebFilter(filterName = "a_exception_handler", value = "/*")} -
 * Specifies the filter name and URL pattern to which the filter should be applied.
 * {@code @ApplicationScoped} - Specifies that the filter instance is application-scoped.
 * {@code @Slf4j} - Lombok annotation for automatic generation of a logger field.
 *
 * <p>The filter initializes a map of exception classes and their corresponding HTTP status codes
 * in the {@code init} method. The exceptions and status codes are used during the filter processing.
 *
 * <p>The filter class is injected with a {@code Converter} instance using the {@code @Inject} annotation.
 *
 * <p>The main logic of the filter is in the {@code doFilter} method, where it invokes the
 * {@code doFilter} method of the next filter in the chain. If a runtime exception occurs during
 * the processing, it checks if the exception type is in the predefined map. If yes, it customizes
 * the HTTP response with the appropriate status code and error message. Otherwise, it logs the
 * exception and rethrows it.
 *
 * @author Eugene Kulik
 * @see Filter
 * @see Converter
 * @see WebFilter
 * @see ApplicationScoped
 * @see Slf4j
 * @see Loggable
 */
@WebFilter(filterName = "a_exception_handler", value = "/*")
@ApplicationScoped
@Slf4j
public class ExceptionHandlerFilter implements Filter {

    private Converter converter;
    private Map<Class<? extends Exception>, Integer> exceptions;

    /**
     * Injects the {@code Converter} instance into the filter.
     *
     * @param converter The {@code Converter} instance to be injected.
     */
    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }
    /**
     * Default list of exception classes to catch.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        exceptions = new HashMap<>();
        exceptions.put(RegistrationException.class, 400);
        exceptions.put(AuthenticationException.class, 400);
        exceptions.put(AccessDeniedException.class, 403);
        exceptions.put(ValidationException.class, 400);
        exceptions.put(UnsupportedMediaTypeException.class, 415);
    }

    /**
     * Processes the request by invoking the next filter in the chain. Catches runtime exceptions
     * and customizes the HTTP response accordingly.
     *
     * @param servletRequest  The request object.
     * @param servletResponse The response object.
     * @param filterChain     The filter chain.
     * @throws IOException      If an I/O error occurs.
     * @throws ServletException If a servlet-related exception occurs.
     */
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
