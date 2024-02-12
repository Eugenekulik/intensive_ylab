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
    private Converter converter;

    @Inject
    public void inject(AgreementService agreementService, ValidationService validationService, 
                       AgreementMapper mapper, Converter converter) {
        this.agreementService = agreementService;
        this.validationService = validationService;
        this.mapper = mapper;
        this.converter = converter;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<Agreement> agreements = agreementService.getPage(new Pageable(page, count));
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

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AgreementDto agreementDto = converter.getRequestBody(req, AgreementDto.class);
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
                .append(converter
                    .convertObjectToJson(mapper
                        .toAgreementDto(agreement)));
            resp.setStatus(201);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO
        }
    }

}
