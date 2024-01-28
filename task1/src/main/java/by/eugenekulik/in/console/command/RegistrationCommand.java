package by.eugenekulik.in.console.command;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.service.UserService;

import java.util.HashSet;
import java.util.List;

public class RegistrationCommand implements Command{

    private HashSet<String> allowedParams = new HashSet<>();

    private UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
        allowedParams.addAll(List.of("username", "password", "email"));
    }

    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.GUEST);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        User user = User.builder()
            .username(requestData.getParams().get("username"))
            .email(requestData.getParams().get("email"))
            .password(requestData.getParams().get("password"))
            .build();
        userService.register(user);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Registration successful!"));
        Session.getResponceData().add("Hello, " + user.getUsername());
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
