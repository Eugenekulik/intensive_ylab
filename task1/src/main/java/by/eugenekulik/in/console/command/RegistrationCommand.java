package by.eugenekulik.in.console.command;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;

/**
 * The {@code RegistrationCommand} class represents a command to register a new user.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class RegistrationCommand implements Command {

    private HashSet<String> allowedParams = new HashSet<>();

    private UserService userService;

    /**
     * Constructs a {@code RegistrationCommand} with the provided {@link UserServiceImpl} for user registration.
     *
     * @param userService The service responsible for user registration.
     */
    public RegistrationCommand(UserService userService) {
        this.userService = userService;
        allowedParams.addAll(List.of("username", "password", "email"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.GUEST);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method extracts user information from the request parameters,
     * registers the user using the provided {@link UserServiceImpl}, and adds success
     * messages to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        User user = User.builder()
            .username(requestData.getParam("username"))
            .email(requestData.getParam("email"))
            .password(requestData.getParam("password"))
            .build();
        userService.register(user);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Registration successful!"));
        Session.getResponceData().add("Hello, " + user.getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
