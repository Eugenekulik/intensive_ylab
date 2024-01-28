package by.eugenekulik.in.console.command.client;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateMetersDataCommand implements Command {


    private final MetersDataService metersDataService;
    private final MetersTypeService metersTypeService;
    private final Set<String> allowedParams = new HashSet<>();

    public CreateMetersDataCommand(MetersDataService metersDataService, MetersTypeService metersTypeService) {
        this.metersDataService = metersDataService;
        this.metersTypeService = metersTypeService;
        allowedParams.addAll(List.of("agreement", "type", "value"));
    }


    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.CLIENT);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        MetersType metersType = metersTypeService.findByName(requestData.getParams().get("type"));
        MetersData metersData = MetersData.builder()
            .metersTypeId(metersType.getId())
            .value(Double.valueOf(requestData.getParams().get("value")))
            .agreementId(Long.valueOf(requestData.getParams().get("agreement")))
            .build();
        metersDataService.create(metersData);
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Creation successful"));
        Session.getResponceData().add(String.format(
            "MetersData {id: %s, agreementId: %s, type: %s, value: %s, placesAt: %s}",
            metersData.getId(), metersData.getAgreementId(), metersType.getName(),
            metersData.getValue(), metersData.getPlacedAt()
        ));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
