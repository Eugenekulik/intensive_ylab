package by.eugenekulik.in.servlet;


import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.utils.Converter;
import by.eugenekulik.service.ValidationService;
import by.eugenekulik.service.logic.AgreementService;
import by.eugenekulik.service.mapper.AgreementMapper;
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

@WebServlet("/agreement")
@NoArgsConstructor
@ApplicationScoped
public class AgreementServlet extends HttpServlet {
    private AgreementService agreementService;
    private ValidationService validationService;
    private AgreementMapper mapper;

    @Inject
    public void inject(AgreementService agreementService, ValidationService validationService, AgreementMapper mapper) {
        this.agreementService = agreementService;
        this.validationService = validationService;
        this.mapper = mapper;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = Converter.getInteger(req, "page");
        int count = Converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<Agreement> agreements = agreementService.getPage(new Pageable(page, count));
        try {
            resp.getWriter()
                .append(Converter.convertObjectToJson(
                    agreements.stream().map(mapper::toAgreementDto)
                        .toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AgreementDto agreementDto = Converter.getRequestBody(req, AgreementDto.class);
        Set<ConstraintViolation<AgreementDto>> errors = validationService.validateObject(agreementDto);
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        Agreement agreement = agreementService.create(mapper.toAgreement(agreementDto));
        try {
            resp.getWriter()
                .append(Converter
                    .convertObjectToJson(mapper
                        .toAgreementDto(agreement)));
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }

}
