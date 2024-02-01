package by.eugenekulik.in.console.command;

import by.eugenekulik.model.User;

/**
 * The ExitCommand class implements the Command interface to provide functionality
 * for exiting the program.
 *
 * @author Eugene Kulik
 */
public class ExitCommand implements Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method for the {@code ExitCommand} terminates the application.
     * </p>
     */
    @Override
    public void execute() {
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return false;
    }
}
