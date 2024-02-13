package by.eugenekulik.service.logic.impl;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.Timed;
import by.eugenekulik.service.aspect.Transactional;
import by.eugenekulik.service.logic.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Service class for handling operations related to users.
 * Manages the interaction with the underlying UserRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
@Timed
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Registers a new user if a user with the same username or email doesn't already exist.
     * Sets the user role to CLIENT.
     *
     * @param user The user to be registered.
     * @return The registered user.
     * @throws RegistrationException If a user with the same username or email already exists.
     */
    @Override
    @Transactional
    public User register(User user) {
        try {
            user.setRole(Role.CLIENT);
            return userRepository.save(user);
        } catch (DatabaseInterectionException e) {
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
        return user;
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageable class with information about page number and count number
     * @return A list of users for the specified page and count.
     * @throws IllegalArgumentException If page is negative or if count is less than 1.
     */
    @Override
    public List<User> getPage(Pageable pageable) {
        return userRepository.getPage(pageable);
    }
}
