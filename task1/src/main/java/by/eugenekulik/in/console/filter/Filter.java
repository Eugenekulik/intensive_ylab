package by.eugenekulik.in.console.filter;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponceData;
import lombok.Setter;

public abstract class Filter {

    @Setter
    private Filter next;

    abstract void service(RequestData requestData, ResponceData responceData, Filter next);
    public void doFilter(RequestData requestData, ResponceData responceData){
        service(requestData, responceData, next);
    }

}
