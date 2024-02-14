package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.User;
import by.eugenekulik.service.annotation.AllowedRoles;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.UserMapper;
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
 * {@code UserServlet} is a servlet class that handles HTTP GET requests
 * related to retrieving user information. It is annotated with {@code @WebServlet}
 * to define the servlet mapping for the "/user" URL and {@code @ApplicationScoped}
 * to specify that the servlet instance is application-scoped.
 *
 * <p>The servlet relies on various injected services and components, such as
 * {@code UserService}, {@code UserMapper}, and {@code Converter}.
 * These dependencies are injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method retrieves user information, responds with a JSON representation of the users,
 * and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see UserService
 * @see UserMapper
 * @see Converter
 * @see Auditable
 * @see AllowedRoles
 */
@WebServlet("/user")
@NoArgsConstructor
@ApplicationScoped
public class UserServlet extends HttpServlet {

    private UserService userService;
    private UserMapper mapper;
    private Converter converter;

    @Inject
    public void inject(UserService userService, UserMapper userMapper, Converter converter) {
        this.userService = userService;
        this.mapper = userMapper;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for retrieving user information.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    @Auditable
    @AllowedRoles({Role.ADMIN})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int page = converter.getInteger(req, "page");
        int count = converter.getInteger(req, "count");
        if (count == 0) count = 10;
        List<User> users = userService.getPage(new Pageable(page, count));
        try {
            resp.getWriter()
                .append(converter.convertObjectToJson(
                    users.stream().map(mapper::fromUser).toList()));
            resp.setStatus(200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
