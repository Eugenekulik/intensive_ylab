package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.service.UserService;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.IOException;
import java.util.Set;

/**
 * {@code AuthorizationServlet} is a servlet class that handles HTTP POST requests
 * related to user authorization. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/authorize" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code UserService}, {@code ValidationService}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doPost} for handling HTTP POST requests.
 * This method is annotated with custom annotations {@code @Auditable} and {@code @AllowedRoles}
 * to denote auditing and role-based access control, respectively.
 *
 * <p>The servlet performs user authorization based on the provided credentials and sets
 * the authenticated user in the session. It also responds with a JSON message indicating
 * the success of the authentication.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see UserService
 * @see ValidationService
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/authorize")
@NoArgsConstructor
@ApplicationScoped
public class AuthorizationServlet extends HttpServlet {
    private UserService userService;
    private ValidationService validationService;
    private Converter converter;

    @Inject
    public void inject(UserService userService, ValidationService validationService, Converter converter) {
        this.userService = userService;
        this.converter = converter;
        this.validationService = validationService;
    }

    /**
     * Handles HTTP POST requests for user authorization.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.GUEST})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AuthDto authDto = converter.getRequestBody(req, AuthDto.class);
        Set<ConstraintViolation<AuthDto>> errors = validationService.validateObject(authDto);
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        User user = userService.authorize(authDto.username, authDto.password);
        req.getSession()
            .setAttribute("authentication",
                new Authentication(user));
        resp.setStatus(200);
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson("Authentication successful!"));
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO change more specific exception
        }
    }

    private record AuthDto(
        @NotNull
        @Length(min = 4, max = 50)
        @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
        String username,
        @NotNull
        @Length(min = 8, max = 128)
        String password
    ) {
    }
}
