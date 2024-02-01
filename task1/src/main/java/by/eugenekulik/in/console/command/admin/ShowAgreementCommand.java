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

/**
 * The {@code ShowAgreementCommand} class represents a command to display a list of agreements.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class ShowAgreementCommand implements Command {

    private final AgreementService agreementService;
    private final Set<String> allowedParams = new HashSet<>();

    /**
     * Constructs a {@code ShowAgreementCommand} with the provided service for managing agreements.
     *
     * @param agreementService The service responsible for managing agreements.
     */
    public ShowAgreementCommand(AgreementService agreementService) {
        this.agreementService = agreementService;
        allowedParams.addAll(List.of("page", "count"));
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
     * The {@code execute} method retrieves request parameters, validates them, fetches a page of agreements, and adds
     * information about each agreement to the response data.
     * </p>
     */
    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        int count = Optional.ofNullable(requestData.getParam("count"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Wrong param value: count should be a integer number", ex);
                }
            })
            .orElse(5);
        int page = Optional.ofNullable(requestData.getParam("page"))
            .map(e -> {
                try {
                    return Integer.parseInt(e);
                } catch (NumberFormatException ex){
                    throw new IllegalArgumentException("Wrong param value: page should be a integer number", ex);
                }
            })
            .orElse(0);
        List<Agreement> agreements = agreementService.getPage(page, count);
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
        return allowedParams.contains(name);
    }
}
