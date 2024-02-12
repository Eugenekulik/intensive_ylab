package by.eugenekulik.in.servlet;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.AgreementService;
import by.eugenekulik.service.logic.MetersDataService;
import by.eugenekulik.service.logic.MetersTypeService;
import by.eugenekulik.service.mapper.MetersDataMapper;
import by.eugenekulik.utils.Converter;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/md/last")
public class UserMetersDataLastServlet extends HttpServlet {

    private MetersDataMapper mapper;
    private MetersDataService metersDataService;
    private MetersTypeService metersTypeService;
    private AgreementService agreementService;
    private Converter converter;

    @Inject
    public void inject(MetersDataMapper mapper, MetersDataService metersDataService, Converter converter,
                       MetersTypeService metersTypeService, AgreementService agreementService) {
        this.mapper = mapper;
        this.metersDataService = metersDataService;
        this.metersTypeService = metersTypeService;
        this.agreementService = agreementService;
        this.converter = converter;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long agreementId = converter.getLong(req, "agreementId");
        Agreement agreement = agreementService.findById(agreementId);
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        if (!agreement.getUserId().equals(authentication.getUser().getId())) {
            throw new AccessDeniedException("not valid agreement id: " + agreementId);
        }
        String type = req.getParameter("type");
        MetersType metersType = metersTypeService.findByName(type);
        MetersData metersData = metersDataService
            .findLastByAgreementAndType(agreementId, metersType.getId());
        try {
            resp.getWriter()
                .append(converter
                    .convertObjectToJson(mapper
                        .toMetersDataDto(metersData)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
