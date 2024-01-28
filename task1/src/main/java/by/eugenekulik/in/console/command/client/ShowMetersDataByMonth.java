package by.eugenekulik.in.console.command.client;

import by.eugenekulik.in.console.RequestData;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.in.console.TextColor;
import by.eugenekulik.in.console.command.Command;
import by.eugenekulik.model.*;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowMetersDataByMonth implements Command {

    private final MetersDataService metersDataService;
    private final MetersTypeService metersTypeService;
    private final AgreementService agreementService;

    public ShowMetersDataByMonth(MetersDataService metersDataService, MetersTypeService metersTypeService, AgreementService agreementService) {
        this.metersDataService = metersDataService;
        this.metersTypeService = metersTypeService;
        this.agreementService = agreementService;
        Set<String> allowedParams = new HashSet<>();
        allowedParams.addAll(List.of("type", "month", "agreementId"));
    }


    @Override
    public boolean isAllowed(User user) {
        return user.getRole().equals(Role.CLIENT);
    }

    @Override
    public void execute() {
        RequestData requestData = Session.getRequestData();
        Long agreementId = Optional.ofNullable(requestData.getParams().get("agreementId"))
            .map(Long::valueOf).orElseThrow(()->new IllegalArgumentException("Param 'agreementId' is required."));
        Agreement agreement = agreementService.findById(agreementId);
        String metersTypeName = Optional.ofNullable(requestData.getParams().get("type"))
            .orElseThrow(()->new IllegalArgumentException("Param 'type' is required."));
        MetersType metersType = metersTypeService.findByName(metersTypeName);
        User user = Session.getCurrentUser();
        if(!agreement.getUserId().equals(user.getId()))
            throw new IllegalArgumentException("not found agreement with id: " + agreementId);
        MetersData metersData;
        if(requestData.getParams().get("month") == null){
            metersData = metersDataService
                .findLastByAgreementAndType(agreement.getId(), metersType.getId());
        } else{
            LocalDate date = LocalDate.parse(requestData.getParams().get("month"),
                DateTimeFormatter.ofPattern("MM.yyyy"));
            metersData = metersDataService
                .findByAgreementAndTypeAndMonth(agreementId, metersType.getId(), date);
        }
        Session.getResponceData().add(TextColor.ANSI_GREEN.changeColor("Result: "));
        Session.getResponceData().add(String.format(
            "id: %s, agreementId: %s, type: %s, value: %s, placedAt: %s",
            metersData.getId(), metersData.getAgreementId(), metersType.getName(),
            metersData.getValue(), metersData.getPlacedAt()
        ));
    }

    @Override
    public boolean isAllowedParam(String name, String value) {
        return false;
    }
}
