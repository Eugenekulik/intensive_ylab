package by.eugenekulik.in.console.filter;

import by.eugenekulik.exception.*;
import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponceData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandlerFilter extends Filter {


    private List<Class<? extends Exception>> exceptions = new ArrayList<>();
    public ExceptionHandlerFilter(){
        exceptions.add(RegistrationException.class);
        exceptions.add(AuthenticationException.class);
        exceptions.add(AccessDeniedException.class);
        exceptions.add(ValidationException.class);
    }

    @Override
    void service(RequestData requestData, ResponceData responceData, Filter next) {
        try{
            if(next!= null)
                next.doFilter(requestData, responceData);
            else
                ((Command)Session.getRequestData().getAttributes().get("command")).execute();
        } catch (RuntimeException exception){
            if(exceptions.contains(exception.getClass())){
                Session.getResponceData().add(TextColor.ANSI_RED.changeColor(exception.getMessage()));
            } else{
                throw new RuntimeException(exception);
            }
        }
    }
}
