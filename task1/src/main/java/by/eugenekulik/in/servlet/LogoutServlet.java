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

/**
 * {@code LogoutServlet} is a servlet class that handles HTTP GET requests
 * related to user logout. It is annotated with {@code @WebServlet} to define the
 * servlet mapping for the "/logout" URL and {@code @ApplicationScoped} to specify that
 * the servlet instance is application-scoped.
 *
 * <p>The servlet relies on the injected {@code Converter} for handling object-to-JSON conversion.
 * The dependency is injected using the {@code @Inject} annotation on the {@code inject} method.
 *
 * <p>The servlet includes a main method: {@code doGet} for handling HTTP GET requests.
 * This method resets the user authentication in the session, responds with a JSON message
 * indicating successful logout, and sets the HTTP response status to 200.
 *
 * @author Eugene Kulik
 * @see HttpServlet
 * @see Converter
 */
@WebServlet("/logout")
@NoArgsConstructor
@ApplicationScoped
public class LogoutServlet extends HttpServlet {

    private Converter converter;

    @Inject
    public void inject(Converter converter) {
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests for user logout.
     *
     * @param req  The {@code HttpServletRequest} object.
     * @param resp The {@code HttpServletResponse} object.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("authentication", new Authentication(User.guest));
        try {
            resp.getWriter().append(converter.convertObjectToJson("Logout"));
            resp.setStatus(200);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
