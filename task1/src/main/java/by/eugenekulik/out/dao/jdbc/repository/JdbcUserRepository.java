package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.UserExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserExtractor extractor;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = new UserExtractor();
    }


    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT users.id, users.username, users.password, users.email, users.role
                    FROM users
                    WHERE users.id = ?;
                """, extractor, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT users.id, users.username, users.password, users.email, users.role
                    FROM users
                    WHERE users.username = ?;
                """, extractor, username));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT users.id, users.username, users.password, users.email, users.role
                    FROM users
                    WHERE users.email = ?;
                """, extractor, email));
    }

    @Override
    public User save(User user) {
        Long id = jdbcTemplate.query("SELECT nextval ('user_sequence');",
            rs -> {
                rs.next();
                return rs.getLong(1);
            });
        user.setId(id);
        jdbcTemplate.update(
            """
                    INSERT INTO users(id, username, password, email, role)
                    VALUES(?, ?, ?, ?, ?);
                """,
            user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole().name()
        );
        return user;
    }

    @Override
    public List<User> getPage(int page, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count should be positive");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page should be positive");
        }
        return jdbcTemplate.query(
            """
                    SELECT users.id, users.username, users.password, users.email, users.role
                    FROM users
                    ORDER BY users.id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), count, page, count);

    }
}
