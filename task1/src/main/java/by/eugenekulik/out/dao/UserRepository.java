package by.eugenekulik.out.dao;

import by.eugenekulik.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The {@code UserRepository} interface defines methods for interacting
 * with user-related data in a repository.
 */
public interface UserRepository {

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return An {@code Optional} containing the user, or empty if not found.
     */
    Optional<User> findById(Long id);

    /**
     * Retrieves a user by its username.
     *
     * @param username The username of the user.
     * @return An {@code Optional} containing the user, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by its email address.
     *
     * @param email The email address of the user.
     * @return An {@code Optional} containing the user, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Saves a user in the repository.
     *
     * @param user The user to save.
     * @return The saved user.
     */
    User save(User user);

    /**
     * Retrieves a page of users from the repository.
     *
     * @param page  The page number (0-indexed).
     * @param count The number of users per page.
     * @return A list of users for the specified page.
     */
    List<User> getPage(int page, int count);
}
