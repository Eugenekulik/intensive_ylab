package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.*;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code ShowMetersDataCommand} class represents a command to display a list of meters data.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class ShowMetersDataCommand implements Command {
    private final MetersDataService metersDataService;
    private final MetersTypeService metersTypeService;
    private final Set<String> allowedParams = new HashSet<>();

    /**
     * Constructs a {@code ShowMetersDataCommand} with the provided services for managing
     * meters data and meters types.
     *
     * @param metersDataService The service responsible for managing meters data.
     * @param metersTypeService The service responsible for managing meters types.
     */
    public ShowMetersDataCommand(MetersDataService metersDataService, MetersTypeService metersTypeService) {
        this.metersDataService = metersDataService;
        this.metersTypeService = metersTypeService;
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
     * The {@code execute} method retrieves request parameters, validates them, fetches a page of meters data, and adds
     * information about each meters data to the response data.
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
        List<MetersData> metersData = metersDataService.getPage(page, count);
        List<MetersType> metersTypes = metersTypeService.findAll();
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        metersData.forEach(md -> Session.getResponceData().add(String.format(
            "id: %s, agreementId: %s, type: %s, placedAt: %s",
            md.getId(), md.getAgreementId(),
            metersTypes.stream().filter(mt -> mt.getId().equals(md.getMetersTypeId())).findAny().get(),
            md.getPlacedAt()
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
