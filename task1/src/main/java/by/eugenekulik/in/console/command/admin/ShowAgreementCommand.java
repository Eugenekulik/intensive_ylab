package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AgreementService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowAgreementCommand implements Command {

    private final AgreementService agreementService;
    private final Set<String> allowedParams = new HashSet<>();

    public ShowAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
        allowedParams.addAll(List.of("page","count"));
    }

    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        int count = Optional.ofNullable(requestData.getParams().get("count"))
            .map(Integer::parseInt)
            .orElse(5);
        int page = Optional.ofNullable(requestData.getParams().get("page"))
            .map(Integer::parseInt)
            .orElse(0);
        List<Agreement> agreements = agreementService.getPage(page, count);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        agreements.forEach(agreement -> Session.getResponceData().add(String.format(
            "id: %s, userId: %s, addressId: %s",
            agreement.getId(), agreement.getUserId(), agreement.getAddressId()
        )));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
