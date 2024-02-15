package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.exception.AccessDeniedException;
import by.eugenekulik.model.Role;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.utils.Converter;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * {@code UserMetersDataLastServlet} is a servlet class that handles HTTP GET requests
 * related to retrieving the last recorded meters data for a specific user and meters type.
 * It is annotated with {@code @WebServlet} to define the servlet mapping for the "/user/md/last" URL.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code MetersDataMapper}, {@code MetersDataService}, {@code Converter},
 * {@code MetersTypeService}, and {@code AgreementService}. These dependencies are injected
 * using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method retrieves the last recorded meters data for a specified agreement and meters type
 * associated with the authenticated user, responds with a JSON representation of the meters data,
 * and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see MetersDataMapper
 * @see MetersDataService
 * @see Converter
 * @see MetersTypeService
 * @see AgreementService
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/user/md/last")
public class UserMetersDataLastServlet extends HttpServlet {

    private MetersDataService metersDataService;
    private AgreementService agreementService;
    private Converter converter;

    @Inject
    public void inject(MetersDataService metersDataService, Converter converter, AgreementService agreementService) {
        this.metersDataService = metersDataService;
        this.agreementService = agreementService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving the last recorded meters data
     * associated with the authenticated user, agreement, and meters type.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long agreementId = converter.getLong(req, "agreementId");
        AgreementDto agreement = agreementService.findById(agreementId);
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        if (!agreement.userId().equals(authentication.getUser().getId())) {
            throw new AccessDeniedException("not valid agreement id: " + agreementId);
        }
        String type = req.getParameter("type");
        MetersDataDto metersData = metersDataService
            .findLastByAgreementAndType(agreementId, type);
        resp.getWriter()
            .append(converter
                .convertObjectToJson(metersData));
        resp.setStatus(200);
    }
}
