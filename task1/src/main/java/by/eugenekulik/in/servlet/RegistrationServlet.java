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

@WebServlet("/register")
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
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }


}
