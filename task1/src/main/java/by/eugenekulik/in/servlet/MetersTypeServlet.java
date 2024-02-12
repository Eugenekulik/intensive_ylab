package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.model.Role;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.MetersTypeService;
import by.eugenekulik.service.mapper.MetersTypeMapper;
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

@WebServlet("/meterstype")
@NoArgsConstructor
@ApplicationScoped
public class MetersTypeServlet extends HttpServlet {

    private MetersTypeService metersTypeService;
    private ValidationService validationService;
    private MetersTypeMapper mapper;
    private Converter converter;

    @Inject
    public void inject(MetersTypeService metersTypeService, ValidationService validationService,
                       MetersTypeMapper mapper, Converter converter) {
        this.metersTypeService = metersTypeService;
        this.validationService = validationService;
        this.mapper = mapper;
        this.converter = converter;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN, Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<MetersType> metersTypes = metersTypeService.findAll();
        try {
            resp.getWriter().append(converter
                .convertObjectToJson(metersTypes.stream().map(mapper::toMetersTypeDto).toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MetersTypeDto metersTypeDto = converter.getRequestBody(req, MetersTypeDto.class);
        Set<ConstraintViolation<MetersTypeDto>> errors = validationService.validateObject(metersTypeDto, "id");
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        MetersType metersType = mapper.toMetersType(metersTypeDto);
        try {
            resp.getWriter().append(converter
                .convertObjectToJson(mapper
                    .toMetersTypeDto(metersTypeService
                        .create(metersType))));
        } catch (IOException e) {
            throw new RuntimeException();//TODO
        }


    }
}
