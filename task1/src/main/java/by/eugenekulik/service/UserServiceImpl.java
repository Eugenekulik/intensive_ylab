package by.eugenekulik.service;

import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;

import java.util.List;

/**
 * Service class for handling operations related to users.
 * Manages the interaction with the underlying UserRepository.
 *
 * @author Eugene Kulik
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionManager transactionManager;

    /**
     * Constructs a UserService with the specified UserRepository.
     *
     * @param userRepository The repository responsible for managing users.
     * @param transactionManager  It is needed to wrap certain database interaction logic in a transaction.
     */
    public UserServiceImpl(UserRepository userRepository, TransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }

    /**
     * Registers a new user if a user with the same username or email doesn't already exist.
     * Sets the user role to CLIENT and logs in the registered user.
     *
     * @param user The user to be registered.
     * @return The registered user.
     * @throws RegistrationException If a user with the same username or email already exists.
     */
    @Override
    public User register(User user) {
        try {
            user.setRole(Role.CLIENT);
            User saved = transactionManager.doInTransaction(() -> userRepository.save(user));
            Session.setCurrentUser(saved);
            return saved;
        } catch (DatabaseInterectionException e){
            throw new RegistrationException("user with this username or email already exists");
        }
    }

    /**
     * Authorizes a user by checking the provided username and password.
     * If successful, sets the authorized user as the current user in the session.
     *
     * @param username The username of the user to authorize.
     * @param password The password of the user to authorize.
     * @return The authorized user.
     * @throws AuthenticationException If the username or password is incorrect.
     */
    @Override
    public User authorize(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthenticationException(
                "The username or password you entered is incorrect"));
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("The username or password you entered is incorrect");
        }
        Session.setCurrentUser(user);
        return user;
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param page  The page number (starting from 0).
     * @param count The number of users to retrieve per page.
     * @return A list of users for the specified page and count.
     * @throws IllegalArgumentException If page is negative or if count is less than 1.
     */
    @Override
    public List<User> getPage(int page, int count) {
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (count < 1) {
            throw new IllegalArgumentException("count must be positive");
        }
        return userRepository.getPage(page, count);
    }
}
