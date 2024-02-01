package by.eugenekulik.service;

import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;

import java.util.List;

/**
 * Service class for handling operations related to users.
 * Manages the interaction with the underlying UserRepository.
 * Provides methods for user registration, authorization, and retrieving a paginated list of users.
 *
 * @author Eugene Kulik
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserService with the specified UserRepository.
     *
     * @param userRepository The repository responsible for managing users.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user if a user with the same username or email doesn't already exist.
     * Sets the user role to CLIENT and logs in the registered user.
     *
     * @param user The user to be registered.
     * @return The registered user.
     * @throws RegistrationException If a user with the same username or email already exists.
     */
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RegistrationException("A user with the same name already exists.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RegistrationException("А user with this email is already registered.");
        }
        user.setRole(Role.CLIENT);
        user = userRepository.save(user);
        Session.setCurrentUser(user);
        return user;
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
