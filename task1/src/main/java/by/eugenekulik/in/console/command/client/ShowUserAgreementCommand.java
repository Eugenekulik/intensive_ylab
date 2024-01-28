package by.eugenekulik.in.console.command.client;

import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AgreementService;

import java.util.HashSet;
import java.util.List;

public class ShowUserAgreementCommand implements Command {

    private AgreementService agreementService;

    public ShowUserAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
    }


    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.CLIENT);
    }

    @Override
    public void execute() {
        List<Agreement> agreements = agreementService.findByUser(Session.getCurrentUser().getId());
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        agreements.forEach(agreement -> Session.getResponceData().add(String.format(
            "id: %s, userId: %s, addressId: %s",
            agreement.getId(), agreement.getUserId(), agreement.getAddressId()
        )));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return false;
    }
}
