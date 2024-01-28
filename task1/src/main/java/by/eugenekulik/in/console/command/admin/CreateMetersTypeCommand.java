package by.eugenekulik.in.console.command.admin;

import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;

public class CreateMetersTypeCommand implements Command {

    private final MetersTypeService metersTypeService;

    public CreateMetersTypeCommand(MetersTypeService metersTypeService) {
        this.metersTypeService = metersTypeService;
    }


    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    @Override
    public void execute() {
        MetersType metersType = new MetersType();
        metersType.setName(Session.getRequestData().getParams().get("name"));
        metersType = metersTypeService.create(metersType);
        Session.getResponceData().add("Creation of the metersType was successful:");
        Session.getResponceData().add("metersType(id: " + metersType.getId() + ", name: " + metersType.getName() + ")");
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return name.equals("name");
    }
}
