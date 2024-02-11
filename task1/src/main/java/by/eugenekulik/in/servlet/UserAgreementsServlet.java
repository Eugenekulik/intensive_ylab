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

@WebServlet("/user/agreement")
@ApplicationScoped
@NoArgsConstructor
public class UserAgreementsServlet extends HttpServlet {
    private AgreementService agreementService;
    private AgreementMapper mapper;

    @Inject
    public void inject(AgreementService agreementService, AgreementMapper mapper) {
        this.agreementService = agreementService;
        this.mapper = mapper;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = Converter.getInteger(req, "page");
        int count = Converter.getInteger(req, "count");
        if (count == 0) count = 10;
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        List<Agreement> agreements = agreementService
            .findByUser(authentication.getUser().getId(), new Pageable(page, count));
        try {
            resp.getWriter()
                .append(Converter.convertObjectToJson(
                    agreements.stream().map(mapper::toAgreementDto)
                        .toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
