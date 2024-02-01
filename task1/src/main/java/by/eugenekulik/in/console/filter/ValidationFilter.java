package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.ValidationException;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponseData;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.in.console.Session;

import java.util.Map;

/**
 * Concrete implementation of a validation filter in a chain of responsibility.
 * This filter validates the incoming request by checking if the specified command exists,
 * if the provided parameters are valid for the command.
 * If validation passes, the execution proceeds to the next filter or the command itself.
 *
 * @author Eugene Kulik
 */
public class ValidationFilter extends Filter {

    private Map<String, Command> commands;


    /**
     * Constructs a ValidationFilter with the provided map of commands.
     *
     * @param commands A map containing available commands.
     */
    public ValidationFilter(Map<String, Command> commands) {
        this.commands = commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void service(RequestData requestData, ResponseData responseData, Filter next) {
        String commandName = requestData.getParam("command");
        if (!commands.containsKey(commandName)) {
            throw new ValidationException("wrong command: '" + commandName + "' doesn't exists");
        }
        Command command = commands.get(commandName);
        Session.getRequestData().setAttribute("command", command);
        requestData.getParamsStream()
            .filter(e -> !e.getKey().equals("command"))
            .forEach(e -> {
                if (!command.isAllowedParam(e.getKey(), e.getValue())) {
                    throw new ValidationException("wrong param or value for commmand.(Command: " + commandName +
                        ", param: " + e.getKey() + ", value: " + e.getValue() + ".");
                }
            });
        if (next != null) {
            next.doFilter(requestData, responseData);
        } else {
            command.execute();
        }
    }
}
