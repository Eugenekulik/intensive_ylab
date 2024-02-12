package by.eugenekulik.in.servlet;

import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.utils.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;

@WebServlet("/logout")
@NoArgsConstructor
@ApplicationScoped
public class LogoutServlet extends HttpServlet {

    private Converter converter;

    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("authentication", new Authentication(User.guest));
        try {
            resp.getWriter().append(converter.convertObjectToJson("Logout"));
            resp.setStatus(200);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
