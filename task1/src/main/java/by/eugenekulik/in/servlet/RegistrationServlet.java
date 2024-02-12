package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.UserService;
import by.eugenekulik.service.mapper.UserMapper;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;

import java.io.IOException;
import java.util.Set;

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
    private ValidationService validationService;
    private UserMapper mapper;
    private Converter converter;

    @Inject
    public void inject(UserService userService, ValidationService validationService, UserMapper mapper,
                       Converter converter) {
        this.userService = userService;
        this.validationService = validationService;
        this.mapper = mapper;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegistrationDto registrationDto = converter.getRequestBody(req, RegistrationDto.class);
        Set<ConstraintViolation<RegistrationDto>> errors =
            validationService.validateObject(registrationDto);
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        User user = mapper.fromRegistrationDto(registrationDto);
        userService.register(user);
        req.getSession().setAttribute("authentication", new Authentication(user));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson("Registration successful"));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }
}
