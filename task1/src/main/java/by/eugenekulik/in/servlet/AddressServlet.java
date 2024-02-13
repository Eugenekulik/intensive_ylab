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

/**
 * {@code AddressServlet} is a servlet class that handles HTTP GET and POST requests
 * related to address operations. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/address" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code AddressService}, {@code AddressMapper}, {@code ValidationService}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes two main methods: {@code doGet} for handling HTTP GET requests and
 * {@code doPost} for handling HTTP POST requests. Both methods are annotated with custom
 * annotations {@code @Auditable} and {@code @AllowedRoles} to denote auditing and role-based
 * access control, respectively.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see AddressService
 * @see AddressMapper
 * @see ValidationService
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/address")
@ApplicationScoped
@NoArgsConstructor
public class AddressServlet extends HttpServlet {

    private AddressService addressService;
    private AddressMapper mapper;
    private ValidationService validationService;
    private Converter converter;


    @Inject
    public void inject(AddressService addressService, AddressMapper mapper, 
                       ValidationService validationService, Converter converter) {
        this.addressService = addressService;
        this.mapper = mapper;
        this.validationService = validationService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving a paginated list of addresses.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<Address> addresses = addressService.getPage(new Pageable(page, count));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson(
                    addresses.stream().map(mapper::fromAddress)
                        .toList()
                ));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles HTTP POST requests for creating a new address.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        AddressDto addressDto = converter.getRequestBody(req, AddressDto.class);
        Set<ConstraintViolation<AddressDto>> errors = validationService
            .validateObject(addressDto, "id");
        if (!errors.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("errors: ");
            errors.stream().forEach(e -> message.append(e.getPropertyPath()).append(": ")
                .append(e.getMessage()).append(", "));
            throw new ValidationException(message.toString());
        }
        Address address = mapper.fromAddressDto(addressDto);
        try {
            resp.getWriter()
                .append(converter
                    .convertObjectToJson(mapper
                        .fromAddress(addressService.create(address))));
            resp.setStatus(201);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO
        }
    }
}
