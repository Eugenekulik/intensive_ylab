package by.eugenekulik.in.console.command;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.UserServiceImpl;

import java.util.HashSet;

/**
 * The {@code AuthenticationCommand} class represents a command to authenticate a user.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class AuthenticationCommand implements Command {
    private final HashSet<String> allowedParams = new HashSet<>();
    private final UserService userService;

    /**
     * Constructs an {@code AuthenticationCommand} with the provided {@link UserServiceImpl} for user authentication.
     *
     * @param userService The service responsible for user authentication.
     */
    public AuthenticationCommand(UserService userService) {
        this.userService = userService;
        allowedParams.add("username");
        allowedParams.add("password");
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
     * The {@code execute} method extracts username and password from the request parameters, performs user
     * authentication using the provided {@link UserServiceImpl}, and adds success messages to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        String username = requestData.getParam("username");
        String password = requestData.getParam("password");
        userService.authorize(username, password);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Authentication successful"));
        Session.getResponceData().add("Hello, " + Session.getCurrentUser().getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
