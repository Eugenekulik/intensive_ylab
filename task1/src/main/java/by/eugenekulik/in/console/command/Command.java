package by.eugenekulik.in.console.command;

import by.eugenekulik.model.User;


/**
 * Interface representing a command that can be executed.
 * Defines methods for checking if the command is allowed for a specific user,
 * executing the command, and checking if a specific parameter is allowed for the command.
 * Implementations of this interface should provide their specific logic for these operations.
 *
 * @author Eugene Kulik
 */
public interface Command {

    /**
     * Checks if the command is allowed for the specified user.
     *
     * @param user The user for whom the command's permission is checked.
     * @return true if the command is allowed for the user, false otherwise.
     */
    boolean isAllowed(User user);

    /**
     * Executes the main functionality of the command.
     * Implementing classes should define the specific behavior of this method.
     */
    void execute();

    /**
     * Checks if a specific parameter is allowed for the command.
     *
     * @param name  The name of the parameter.
     * @param value The value of the parameter.
     * @return true if the parameter is allowed for the command, false otherwise.
     */
    boolean isAllowedParam(String name, String value);


}
