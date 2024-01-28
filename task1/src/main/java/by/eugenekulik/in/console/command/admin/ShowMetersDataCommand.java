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

public class ShowMetersDataCommand implements Command {
    private final MetersDataService metersDataService;
    private final MetersTypeService metersTypeService;
    private final Set<String> allowedParams = new HashSet<>();

    public ShowMetersDataCommand(MetersDataService metersDataService, MetersTypeService metersTypeService) {
        this.metersDataService = metersDataService;
        this.metersTypeService = metersTypeService;
        allowedParams.addAll(List.of("page", "count"));
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
        List<MetersData> metersData = metersDataService.getPage(page, count);
        List<MetersType> metersTypes = metersTypeService.findAll();
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result:"));
        metersData.forEach(md -> Session.getResponceData().add(String.format(
            "id: %s, agreementId: %s, type: %s, placedAt: %s",
            md.getId(), md.getAgreementId(),
            metersTypes.stream().filter(mt->mt.getId().equals(md.getMetersTypeId())).findAny().get(),
            md.getPlacedAt()
        )));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return allowedParams.contains(name);
    }
}
