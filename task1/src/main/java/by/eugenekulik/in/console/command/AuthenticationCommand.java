package by.eugenekulik.in.console.command;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.service.UserService;

import java.util.HashSet;

public class AuthenticationCommand implements Command{

    private HashSet<String> allowedParams = new HashSet<>();
    private UserService userService;

    public AuthenticationCommand(UserService userService) {
        this.userService = userService;
        allowedParams.add("username");
        allowedParams.add("password");
    }

    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.GUEST);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        String username = requestData.getParams().get("username");
        String password = requestData.getParams().get("password");
        userService.authorize(username, password);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Authentication successful"));
        Session.getResponceData().add("Hello, " + Session.getCurrentUser().getUsername());
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
