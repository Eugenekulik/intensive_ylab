package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code ShowUserCommand} class represents a command to display a list of users.
 * It implements the {@link Command} interface
 *
 * @author Eugene Kulik
 */
public class ShowUserCommand implements Command {

    private final Set<String> allowedParams = new HashSet<>();
    private final UserService userService;


    /**
     * Constructs a {@code ShowUserCommand} with the provided service for managing users.
     *
     * @param userService The service responsible for managing users.
     */
    public ShowUserCommand(UserService userService) {
        this.userService = userService;
        allowedParams.addAll(List.of("page", "count"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method retrieves request parameters, validates them, fetches
     * a page of users, and adds information about each user to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        int count = Optional.ofNullable(requestData.getParam("count"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Wrong param value: count should be a integer number", ex);
                }
            })
            .orElse(5);
        int page = Optional.ofNullable(requestData.getParam("page"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex){
                    throw new IllegalArgumentException("Wrong param value: page should be a integer number", ex);
                }
            })
            .orElse(0);
        List<User> users = userService.getPage(page, count);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result"));
        users.forEach(user -> Session.getResponceData().add(String.format(
            "id: %s, username: %s, email: %s, role: %s",
            user.getId(), user.getUsername(), user.getEmail(), user.getRole())
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
