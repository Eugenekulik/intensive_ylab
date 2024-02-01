package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.*;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponseData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of an exception handling filter in a chain of responsibility.
 * This filter catches specified runtime exceptions, handles them, and continues the execution chain.
 * If an unexpected exception occurs, it is rethrown as a RuntimeException.
 *
 * @author Eugene Kulik
 */
public class ExceptionHandlerFilter extends Filter {


    private List<Class<? extends Exception>> exceptions = new ArrayList<>();

    /**
     * Constructs an ExceptionHandlerFilter with a default list of exception classes to catch.
     */
    public ExceptionHandlerFilter() {
        exceptions.add(RegistrationException.class);
        exceptions.add(AuthenticationException.class);
        exceptions.add(AccessDeniedException.class);
        exceptions.add(ValidationException.class);
        exceptions.add(MissingInformationException.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void service(RequestData requestData, ResponseData responseData, Filter next) {
        try {
            if (next != null) {
                next.doFilter(requestData, responseData);
            } else {
                ((Command) Session.getRequestData().getAttribute("command")).execute();
            }
        } catch (RuntimeException exception) {
            if (exceptions.contains(exception.getClass())) {
                Session.getResponceData().add(TextColor.ANSI_RED.changeColor(exception.getMessage()));
            } else {
                throw new RuntimeException(exception);
            }
        }
    }
}
