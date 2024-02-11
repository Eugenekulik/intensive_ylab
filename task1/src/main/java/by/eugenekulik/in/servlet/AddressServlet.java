package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.model.Address;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.ValidationService;
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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/address")
@ApplicationScoped
@NoArgsConstructor
public class AddressServlet extends HttpServlet {

    private AddressService addressService;
    private AddressMapper mapper;
    private ValidationService validationService;


    @Inject
    public void inject(AddressService addressService, AddressMapper mapper, ValidationService validationService) {
        this.addressService = addressService;
        this.mapper = mapper;
        this.validationService = validationService;
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = Converter.getInteger(req, "page");
        int count = Converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<Address> addresses = addressService.getPage(new Pageable(page, count));
        try {
            resp.getWriter()
                .append(Converter.convertObjectToJson(
                    addresses.stream().map(mapper::toAddressDto)
                        .toList()
                ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AddressDto addressDto = Converter.getRequestBody(req, AddressDto.class);
        Set<ConstraintViolation<AddressDto>> errors = validationService.validateObject(addressDto);
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        Address address = mapper.toAddress(addressDto);
        try {
            resp.getWriter()
                .append(Converter
                    .convertObjectToJson(mapper
                        .toAddressDto(addressService.create(address))));
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
    }


}
