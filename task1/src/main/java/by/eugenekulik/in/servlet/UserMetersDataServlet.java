package by.eugenekulik.in.servlet;

import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.AgreementService;
import by.eugenekulik.service.logic.MetersDataService;
import by.eugenekulik.service.logic.MetersTypeService;
import by.eugenekulik.service.mapper.MetersDataMapper;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * {@code UserMetersDataServlet} is a servlet class that handles HTTP GET requests
 * related to retrieving meters data for a specific user, agreement, and meters type.
 * It is annotated with {@code @WebServlet} to define the servlet mapping for the "/user/md" URL.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code MetersDataService}, {@code MetersDataMapper}, {@code Converter},
 * {@code AgreementService}, and {@code MetersTypeService}. These dependencies are injected
 * using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method retrieves meters data associated with the authenticated user, agreement, and meters type,
 * responds with a JSON representation of the meters data, and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see MetersDataService
 * @see MetersDataMapper
 * @see Converter
 * @see AgreementService
 * @see MetersTypeService
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/user/md")
@ApplicationScoped
public class UserMetersDataServlet extends HttpServlet {

    private MetersDataService metersDataService;
    private AgreementService agreementService;
    private MetersDataMapper mapper;
    private MetersTypeService metersTypeService;
    private Converter converter;

    @Inject
    public void inject(MetersDataService metersDataService, MetersDataMapper mapper, Converter converter,
                       AgreementService agreementService, MetersTypeService metersTypeService) {
        this.metersDataService = metersDataService;
        this.mapper = mapper;
        this.agreementService = agreementService;
        this.metersTypeService = metersTypeService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving meters data associated with the authenticated user,
     * agreement, and meters type.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        long agreementId = converter.getLong(req, "agreementId");
        Agreement agreement = agreementService.findById(agreementId);
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        if (!agreement.getUserId().equals(authentication.getUser().getId())) {
            throw new AccessDeniedException("not valid agreement id: " + agreementId);
        }
        String type = req.getParameter("type");
        MetersType metersType = metersTypeService.findByName(type);
        if (count == 0) count = 10;
        List<MetersData> metersData = metersDataService
            .findByAgreementAndType(agreementId, metersType.getId(), new Pageable(page, count));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson(
                    metersData.stream().map(mapper::toMetersDataDto)
                        .toList()));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
