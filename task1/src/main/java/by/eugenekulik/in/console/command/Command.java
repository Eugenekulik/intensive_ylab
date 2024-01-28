package by.eugenekulik.in.console.command;

import by.eugenekulik.model.User;

public interface Command {

    boolean isAllowed(User user);
    void execute();


    boolean isAllowedParam(String name, String value);


}
