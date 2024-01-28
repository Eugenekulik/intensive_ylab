package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.ValidationException;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponceData;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.in.console.Session;

import java.util.HashMap;
import java.util.Map;

public class ValidationFilter extends Filter{

    private Map<String, Command> commands;


    public ValidationFilter(Map<String, Command> commands){
        this.commands = commands;
    }


    @Override
    public void service(RequestData requestData, ResponceData responceData, Filter next) {
        String commandName = requestData.getParams().get("command");
        if(!commands.containsKey(commandName))
            throw new ValidationException("wrong command: '" + commandName + "' doesn't exists");
        Command command = commands.get(commandName);
        Session.getRequestData().getAttributes().put("command", command);
        for(Map.Entry<String, String> entry: requestData.getParams().entrySet()){
            if(!entry.getKey().equals("command")
                && !command.isAllowedParam(entry.getKey(), entry.getValue())) {
                throw new ValidationException("wrong param or value for commmand.(Command: " + commandName +
                    ", param: " + entry.getKey() + ", value: " + entry.getValue() + ".");
            }
        }
        if(next != null)
            next.doFilter(requestData, responceData);
        else command.execute();
    }
}
