package by.eugenekulik.in.console.command;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;

public class LogoutCommand implements Command{



    @Override
    public boolean isAllowed(User user) {
        return !user.getRole().equals(Role.GUEST);
    }

    @Override
    public void execute() {
        Session.setCurrentUser(User.guest());
        Session.getResponceData().add("Logout");
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return true;
    }
}
