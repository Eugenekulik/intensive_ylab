package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.service.UserMapper;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * {@code RegistrationServlet} is a servlet class that handles HTTP POST requests
 * related to user registration. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/register" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code UserService}, {@code ValidationService}, {@code UserMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doPost} for handling HTTP POST requests.
 * This method processes user registration based on the provided registration data,
 * responds with a JSON message indicating successful registration, and sets the HTTP response
 * status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see UserService
 * @see ValidationService
 * @see UserMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/register")
@ApplicationScoped
public class RegistrationServlet extends HttpServlet {
    private UserService userService;
    private Converter converter;

    @Inject
    public void inject(UserService userService, Converter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    /**
     * Handles HTTP POST requests for user registration.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     * @throws ServletException If a servlet-related exception occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.GUEST})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RegistrationDto registrationDto = converter.getRequestBody(req, RegistrationDto.class);
        UserDto userDto = userService.register(registrationDto);
        resp.getWriter()
            .append(converter
                .convertObjectToJson(new Object[]{"Registration successful", userDto}));
        resp.setStatus(200);
    }
}
