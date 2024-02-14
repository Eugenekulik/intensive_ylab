package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.service.MetersTypeMapper;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * {@code MetersTypeServlet} is a servlet class that handles HTTP GET and POST requests
 * related to meters type operations. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/meterstype" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code MetersTypeService}, {@code ValidationService}, {@code MetersTypeMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes two main methods: {@code doGet} for handling HTTP GET requests and
 * {@code doPost} for handling HTTP POST requests. Both methods are annotated with custom
 * annotations {@code @Auditable} and {@code @AllowedRoles} to denote auditing and role-based
 * access control, respectively.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see MetersTypeService
 * @see ValidationService
 * @see MetersTypeMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/meterstype")
@NoArgsConstructor
@ApplicationScoped
public class MetersTypeServlet extends HttpServlet {

    private MetersTypeService metersTypeService;
    private ValidationService validationService;
    private MetersTypeMapper mapper;
    private Converter converter;

    @Inject
    public void inject(MetersTypeService metersTypeService, MetersTypeMapper mapper,
                       ValidationService validationService, Converter converter) {
        this.metersTypeService = metersTypeService;
        this.validationService = validationService;
        this.mapper = mapper;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving a list of all meters types.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN, Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<MetersType> metersTypes = metersTypeService.findAll();
        try {
            resp.getWriter().append(converter
                .convertObjectToJson(metersTypes.stream().map(mapper::fromMetersType).toList()));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    /**
     * Handles HTTP POST requests for creating a new meters type.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MetersTypeDto metersTypeDto = converter.getRequestBody(req, MetersTypeDto.class);
        Set<ConstraintViolation<MetersTypeDto>> errors = validationService
            .validateObject(metersTypeDto, "id");
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        MetersType metersType = mapper.fromMetersTypeDto(metersTypeDto);
        try {
            resp.getWriter().append(converter
                .convertObjectToJson(mapper
                    .fromMetersType(metersTypeService
                        .create(metersType))));
            resp.setStatus(201);
        } catch (IOException e) {
            throw new RuntimeException();//TODO
        }
    }
}
