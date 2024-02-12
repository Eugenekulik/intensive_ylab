package by.eugenekulik.in.servlet;

import by.eugenekulik.model.Role;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.User;
import by.eugenekulik.service.aspect.AllowedRoles;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.logic.UserService;
import by.eugenekulik.service.mapper.UserMapper;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
