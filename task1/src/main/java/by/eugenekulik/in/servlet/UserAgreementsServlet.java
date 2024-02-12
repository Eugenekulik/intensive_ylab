package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.AgreementService;
import by.eugenekulik.service.mapper.AgreementMapper;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * {@code UserAgreementsServlet} is a servlet class that handles HTTP GET requests
 * related to retrieving agreements for a specific user. It is annotated with {@code @WebServlet}
 * to define the servlet mapping for the "/user/agreement" URL and {@code @ApplicationScoped}
 * to specify that the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code AgreementService}, {@code AgreementMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method retrieves agreements associated with the authenticated user, responds with a JSON
 * representation of the agreements, and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see AgreementService
 * @see AgreementMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/user/agreement")
@ApplicationScoped
@NoArgsConstructor
public class UserAgreementsServlet extends HttpServlet {
    private AgreementService agreementService;
    private AgreementMapper mapper;
    private Converter converter;
    @Inject
    public void inject(AgreementService agreementService, AgreementMapper mapper, Converter converter) {
        this.agreementService = agreementService;
        this.mapper = mapper;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving agreements associated with the authenticated user.
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
        if (count == 0) count = 10;
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        List<Agreement> agreements = agreementService
            .findByUser(authentication.getUser().getId(), new Pageable(page, count));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson(
                    agreements.stream().map(mapper::toAgreementDto)
                        .toList()));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
