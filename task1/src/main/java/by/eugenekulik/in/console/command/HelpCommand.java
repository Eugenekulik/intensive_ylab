package by.eugenekulik.in.console.command;

import by.eugenekulik.in.console.Session;
import by.eugenekulik.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HelpCommand implements Command{

    private Map<String, Command> commands;
    private ResourceBundle resourceBundle;

    public HelpCommand(Map<String, Command> commands, ResourceBundle resourceBundle) {
        this.commands = commands;
        this.resourceBundle = resourceBundle;
    }


    @Override
    public boolean isAllowed(User user) {
        return true;
    }

    @Override
    public void execute() {
        String command = Session.getRequestData().getParams().get("value");
        if(command == null){
            Session.getResponceData().add(resourceBundle.getString("help"));
        } else{
            Session.getResponceData().add(resourceBundle.getString("help." + command));
        }
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return name.equals("value") &&
            commands.containsKey(value);
    }
}
