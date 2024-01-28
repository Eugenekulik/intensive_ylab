package by.eugenekulik.in.console.command;

import by.eugenekulik.model.User;

public class ExitCommand implements Command{
    @Override
    public boolean isAllowed(User user) {
        return true;
    }

    @Override
    public void execute() {
        System.exit(0);
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return false;
    }
}
