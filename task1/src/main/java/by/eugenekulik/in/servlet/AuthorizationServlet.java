package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.UserService;
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
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson("Authentication successful!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
