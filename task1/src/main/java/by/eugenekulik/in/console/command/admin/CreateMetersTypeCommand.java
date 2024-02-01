package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;

/**
 * The {@code CreateMetersTypeCommand} class represents a command to create a new meters type.
 * It implements the {@link Command} interface.
 *
 * @author Eugene Kulik
 */
public class CreateMetersTypeCommand implements Command {

    private final MetersTypeService metersTypeService;

    /**
     * Constructs a {@code CreateMetersTypeCommand} with the provided service for managing meters types.
     *
     * @param metersTypeService The service responsible for managing meters types.
     */
    public CreateMetersTypeCommand(MetersTypeService metersTypeService) {
        this.metersTypeService = metersTypeService;
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
     * The {@code execute} method creates a new meters type, sets its name from the request parameters, and adds
     * information about the created meters type to the response data.
     * </p>
     */
    @Override
    public void execute() {
        MetersType metersType = new MetersType();
        metersType.setName(Session.getRequestData().getParam("name"));
        metersType = metersTypeService.create(metersType);
        Session.getResponceData().add("Creation of the metersType was successful:");
        Session.getResponceData().add("metersType(id: " + metersType.getId() + ", name: " + metersType.getName() + ")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowedParam(String name, String value) {
        return name.equals("name");
    }
}
