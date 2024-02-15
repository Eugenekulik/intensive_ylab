package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
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
 * {@code MetersDataServlet} is a servlet class that handles HTTP GET and POST requests
 * related to meters data operations. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/metersdata" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code ValidationService}, {@code MetersDataService}, {@code MetersDataMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes two main methods: {@code doGet} for handling HTTP GET requests and
 * {@code doPost} for handling HTTP POST requests. Both methods are annotated with custom
 * annotations {@code @Auditable} and {@code @AllowedRoles} to denote auditing and role-based
 * access control, respectively.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see ValidationService
 * @see MetersDataService
 * @see MetersDataMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/metersdata")
@NoArgsConstructor
@ApplicationScoped
public class MetersDataServlet extends HttpServlet {
    private MetersDataService metersDataService;
    private Converter converter;

    @Inject
    public void inject(MetersDataService metersDataService, Converter converter) {
        this.metersDataService = metersDataService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving a paginated list of meters data.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<MetersDataDto> metersData = metersDataService.getPage(new Pageable(page, count));
        resp.getWriter()
            .append(converter.convertObjectToJson(metersData));
        resp.setStatus(200);
    }

    /**
     * Handles HTTP POST requests for creating new meters data.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MetersDataDto metersDataDto = converter.getRequestBody(req, MetersDataDto.class);
        metersDataDto = metersDataService.create(metersDataDto);
        resp.getWriter().append(converter
            .convertObjectToJson(metersDataDto));
        resp.setStatus(201);
    }
}
