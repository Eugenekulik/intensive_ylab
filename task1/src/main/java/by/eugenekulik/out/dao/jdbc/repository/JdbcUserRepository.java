package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.UserExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JdbcUserRepository is a JDBC implementation of the UserRepository interface
 * for performing CRUD operations related to users in the database.
 * The class is annotated with @ApplicationScoped, indicating that it may be managed
 * by a spring framework container.
 *
 * @author Eugene Kulik
 * @see UserRepository
 * @see JdbcTemplate
 */
@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserExtractor extractor;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate, UserExtractor extractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
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
                    OFFSET ?;
                """,
            new ListExtractor<>(extractor), pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());

    }
}
