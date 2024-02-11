package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.MetersDataService;
import by.eugenekulik.service.mapper.MetersDataMapper;
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

@WebServlet("/metersdata")
@NoArgsConstructor
@ApplicationScoped
public class MetersDataServlet extends HttpServlet {

    private ValidationService validationService;
    private MetersDataService metersDataService;
    private MetersDataMapper mapper;

    @Inject
    public void inject(ValidationService validationService, MetersDataService metersDataService,
                       MetersDataMapper mapper) {
        this.validationService = validationService;
        this.metersDataService = metersDataService;
        this.mapper = mapper;
    }


    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = Converter.getInteger(req, "page");
        int count = Converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<MetersData> metersData = metersDataService.getPage(new Pageable(page, count));
        try {
            resp.getWriter().append(Converter
                .convertObjectToJson(
                    metersData.stream().map(mapper::toMetersDataDto).toList()));
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        MetersDataDto metersDataDto = Converter.getRequestBody(req, MetersDataDto.class);
        Set<ConstraintViolation<MetersDataDto>> errors = validationService.validateObject(metersDataDto);
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        MetersData metersData = metersDataService.create(mapper.toMetersData(metersDataDto));
        try {
            resp.getWriter().append(Converter
                .convertObjectToJson(mapper
                    .toMetersDataDto(metersData)));
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
    }
}
