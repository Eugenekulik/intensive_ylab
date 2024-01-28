package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponceData;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.in.console.Session;


public class SecurityFilter extends Filter{




    @Override
    public void service(RequestData requestData, ResponceData responceData, Filter next) {
        Command command = (Command) requestData.getAttributes().get("command");
        if(!command.isAllowed(Session.getCurrentUser())) {
            throw new AccessDeniedException("This user does not have privileges to execute this command");
        }
        if(next != null)
            next.doFilter(requestData, responceData);
        else command.execute();
    }


}
