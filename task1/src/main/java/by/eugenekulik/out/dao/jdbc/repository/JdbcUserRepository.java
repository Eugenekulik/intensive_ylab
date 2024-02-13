package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.UserExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import by.eugenekulik.service.aspect.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * JdbcUserRepository is a JDBC implementation of the UserRepository interface
 * for performing CRUD operations related to users in the database.
 * <p>
 * The class is annotated with @ApplicationScoped, indicating that it may be managed
 * by a CDI (Contexts and Dependency Injection) container.
 * <p>
 * It also uses the @Loggable annotation to enable logging for the methods in the class.
 *
 * @author Eugene Kulik
 * @see UserRepository
 * @see JdbcTemplate
 */
@ApplicationScoped
@NoArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private JdbcTemplate jdbcTemplate;
    private UserExtractor extractor = new UserExtractor();

    @Inject
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    @Loggable
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, username, password, email, role
                    FROM meters.users
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    @Loggable
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, username, password, email, role
                    FROM meters.users
                    WHERE username = ?;
                """, extractor, username));
    }

    @Override
    @Loggable
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, username, password, email, role
                    FROM meters.users
                    WHERE email = ?;
                """, extractor, email));
    }

    @Override
    @Loggable
    public User save(User user) {
        jdbcTemplate.update(
            """
                    INSERT INTO meters.users(id, username, password, email, role)
                    VALUES(nextval('meters.user_sequence'), ?, ?, ?, ?);
                """,
            user.getUsername(), user.getPassword(), user.getEmail(), user.getRole().name()
        );
        user = jdbcTemplate.query("""
                    SELECT id, username, password, email, role
                    FROM meters.users
                    where username = ?;
            """, extractor, user.getUsername());
        return user;
    }

    @Override
    @Loggable
    public List<User> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, username, password, email, role
                    FROM meters.users
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), pageable.getCount(), pageable.getPage(), pageable.getCount());

    }
}
