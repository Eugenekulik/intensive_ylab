package by.eugenekulik.in.servlet;

import by.eugenekulik.model.User;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.utils.Converter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("authentication", new Authentication(User.guest));
        try {
            resp.getWriter().append(Converter.convertObjectToJson("Logout"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
