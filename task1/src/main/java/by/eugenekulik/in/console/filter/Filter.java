package by.eugenekulik.in.console.filter;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponceData;

public abstract class Filter {

    private Filter next;

    public void setNext(Filter next){
        this.next = next;
    }

    abstract void service(RequestData requestData, ResponceData responceData, Filter next);
    public void doFilter(RequestData requestData, ResponceData responceData){
        service(requestData, responceData, next);
    }

}
