package by.eugenekulik.in.console.command.client;

import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.AgreementServiceImpl;

import java.util.List;

/**
 * The {@code ShowUserAgreementCommand} class represents a command to display user agreements.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class ShowUserAgreementCommand implements Command {

    private AgreementService agreementService;


    /**
     * Constructs a {@code ShowUserAgreementCommand} with the provided {@link AgreementServiceImpl} for retrieving agreements.
     *
     * @param agreementService The service responsible for managing user agreements.
     */
    public ShowUserAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.CLIENT);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method retrieves user agreements using the provided {@link AgreementServiceImpl} and adds
     * information about each agreement to the response data.
     * </p>
     */
    @Override
    public void execute() {
        List<Agreement> agreements = agreementService.findByUser(Session.getCurrentUser().getId());
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        agreements.forEach(agreement -> Session.getResponceData().add(String.format(
            "id: %s, userId: %s, addressId: %s",
            agreement.getId(), agreement.getUserId(), agreement.getAddressId()
        )));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return false;
    }
}
