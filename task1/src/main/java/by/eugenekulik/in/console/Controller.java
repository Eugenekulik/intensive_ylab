package by.eugenekulik.in.console;

import by.eugenekulik.in.console.filter.Filter;
import by.eugenekulik.model.User;

public class Controller {


    private final ConsoleMessageConverter converter;
    private final View view;

    private Filter filter;

    public Controller(View view, ConsoleMessageConverter converter, Filter filter){
        this.converter = converter;
        this.view = view;
        this.filter = filter;
    }


    public void start(){
        Session.setCurrentUser(User.guest());
        while(true){
            view.write(TextColor.ANSI_BLUE
                .changeColor("###################################################################################"));
            Session.setRequestData(converter.read(view.read()));
            Session.setResponceData(new ResponceData());
            filter.doFilter(Session.getRequestData(), Session.getResponceData());
            view.write(converter.write(Session.getResponceData()));
            Session.setResponceData(null);
            Session.setRequestData(null);
        }
    }

}
