package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.in.console.Session;

import java.util.HashSet;
import java.util.List;

public class CreateAgreementCommand implements Command {

    private AgreementService agreementService;
    private HashSet<String> allowedParams = new HashSet<>();

    public CreateAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
        allowedParams.addAll(List.of("userId", "addressId"));
    }

    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        Agreement agreement  = Agreement.builder()
            .addressId(Long.parseLong(requestData.getParams().get("addressId")))
            .userId(Long.parseLong(requestData.getParams().get("userId")))
            .build();
        agreementService.create(agreement);
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
