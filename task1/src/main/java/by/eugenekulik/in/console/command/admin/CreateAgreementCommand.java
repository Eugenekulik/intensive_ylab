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

/**
 * The {@code CreateAgreementCommand} class represents a command to create a new agreement.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class CreateAgreementCommand implements Command {

    private AgreementService agreementService;
    private HashSet<String> allowedParams = new HashSet<>();

    /**
     * Constructs a {@code CreateAgreementCommand} with the provided service for managing agreements.
     *
     * @param agreementService The service responsible for managing agreements.
     */
    public CreateAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
        allowedParams.addAll(List.of("userId", "addressId"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }


    /**
     * {@inheritDoc}
     * <p>
     * The {@code execute} method retrieves request parameters, validates them, creates an agreement,
     * and adds information about the created agreement to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        Agreement agreement = Agreement.builder()
            .addressId(Long.parseLong(requestData.getParam("addressId")))
            .userId(Long.parseLong(requestData.getParam("userId")))
            .build();
        agreementService.create(agreement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
