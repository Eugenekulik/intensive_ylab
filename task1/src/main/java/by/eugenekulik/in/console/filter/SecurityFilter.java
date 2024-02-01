package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponseData;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.in.console.Session;


/**
 * Concrete implementation of a security filter in a chain of responsibility.
 * This filter checks if the current user has the necessary privileges to execute a command.
 * If the user is not allowed, an AccessDeniedException is thrown; otherwise,
 * the execution proceeds to the next filter or the command itself.
 *
 * @author Eugene Kulik
 */
public class SecurityFilter extends Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void service(RequestData requestData, ResponseData responseData, Filter next) {
        Command command = (Command) requestData.getAttribute("command");

        if (!command.isAllowed(Session.getCurrentUser())) {
            throw new AccessDeniedException("This user does not have privileges to execute this command");
        }
        if (next != null) {
            next.doFilter(requestData, responseData);
        } else {
            command.execute();
        }
    }
}
