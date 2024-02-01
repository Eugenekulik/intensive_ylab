package by.eugenekulik.in.console.command;

import by.eugenekulik.exception.MissingInformationException;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The {@code HelpCommand} class represents a command to provide information about other commands.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class HelpCommand implements Command {

    private Map<String, Command> commands;
    private ResourceBundle resourceBundle;

    /**
     * Constructs a {@code HelpCommand} with the provided map of commands and {@link ResourceBundle} for help information.
     *
     * @param commands       A map of command names to their respective implementations.
     * @param resourceBundle The resource bundle containing help information.
     */
    public HelpCommand(Map<String, Command> commands, ResourceBundle resourceBundle) {
        this.commands = commands;
        this.resourceBundle = resourceBundle;
    }


    @Override
    public boolean isAllowed(User user) {
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method retrieves and adds help information for a specific command.
     * </p>
     */
    @Override
    public void execute() {
        String command = Session.getRequestData().getParam("value");
        if (command == null) command = "help";
        String info;
        try {
            info = resourceBundle.getString("help." + command);

        } catch (Exception exception) {
            throw new MissingInformationException("not found information about " + command);
        }
        Session.getResponceData().add(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return name.equals("value") &&
            commands.containsKey(value);
    }
}
