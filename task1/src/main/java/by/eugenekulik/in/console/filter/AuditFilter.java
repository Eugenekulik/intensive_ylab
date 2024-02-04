package by.eugenekulik.in.console.filter;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.ResponseData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Rec;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AuditService;

import java.time.LocalDateTime;

public class AuditFilter extends Filter {

    private AuditService auditService;

    public AuditFilter(AuditService auditService) {
        this.auditService = auditService;
    }


    @Override
    void service(RequestData requestData, ResponseData responseData, Filter next) {
        Command command = (Command) requestData.getAttribute("command");
        if (next != null) {
            next.doFilter(requestData, responseData);
        } else {
            command.execute();
        }
        User user = Session.getCurrentUser();
        Rec rec = new Rec(command.getClass().getName(), LocalDateTime.now(), user.getUsername());
        auditService.createRec(rec);
    }
}
