package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Address;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.AddressService;
import by.eugenekulik.service.mapper.AddressMapper;
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


@WebServlet("/user/addresses")
@ApplicationScoped
@NoArgsConstructor
public class UserAddressesServlet extends HttpServlet {

    private AddressService addressService;
    private AddressMapper mapper;
    
    private Converter converter;
    @Inject
    public void inject(AddressService addressService, AddressMapper mapper, Converter converter) {
        this.addressService = addressService;
        this.mapper = mapper;
        this.converter = converter;
    }


    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        List<Address> agreements = addressService
            .findByUser(authentication.getUser().getId(), new Pageable(page, count));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson(
                    agreements.stream().map(mapper::toAddressDto)
                        .toList()));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
