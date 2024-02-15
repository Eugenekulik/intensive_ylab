package by.eugenekulik.in.servlet;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.AddressMapper;
import by.eugenekulik.service.AddressService;
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
 * {@code UserAddressesServlet} is a servlet class that handles HTTP GET requests
 * related to retrieving addresses for a specific user. It is annotated with {@code @WebServlet}
 * to define the servlet mapping for the "/user/addresses" URL and {@code @ApplicationScoped}
 * to specify that the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code AddressService}, {@code AddressMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method retrieves addresses associated with the authenticated user, responds with a JSON
 * representation of the addresses, and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see AddressService
 * @see AddressMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/user/addresses")
@ApplicationScoped
@NoArgsConstructor
public class UserAddressesServlet extends HttpServlet {

    private AddressService addressService;

    private Converter converter;

    @Inject
    public void inject(AddressService addressService, Converter converter) {
        this.addressService = addressService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving addresses associated with the authenticated user.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.CLIENT})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        Authentication authentication = (Authentication) req.getSession().getAttribute("authentication");
        List<AddressDto> addresses = addressService
            .findByUser(authentication.getUser().getId(), new Pageable(page, count));
        resp.getWriter()
            .append(converter.convertObjectToJson(addresses));
        resp.setStatus(200);
    }
}
