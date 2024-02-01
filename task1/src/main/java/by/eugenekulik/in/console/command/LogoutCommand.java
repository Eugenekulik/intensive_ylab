package by.eugenekulik.in.console.command;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.in.console.Session;

/**
 * The {@code LogoutCommand} class represents a command to log out the current user.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class LogoutCommand implements Command {


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return !user.getRole().equals(Role.GUEST);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method sets the current user to a guest user and adds a response
     * message indicating successful logout.
     * </p>
     */
    @Override
    public void execute() {
        Session.setCurrentUser(User.guest());
        Session.getResponceData().add("Logout");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return true;
    }
}
