package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.UserExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String role = user.getRole().toString();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                """
                    INSERT INTO meters.users(id, username, password, email, role)
                    VALUES(nextval('meters.user_sequence'), ?, ?, ?, ?);
                """, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, role);
            return ps;
        }, keyHolder);
        user.setId((Long) keyHolder.getKeys().get("id"));
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
