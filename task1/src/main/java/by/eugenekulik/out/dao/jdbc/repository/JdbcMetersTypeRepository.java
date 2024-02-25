package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersTypeExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcMetersTypeRepository is a JDBC implementation of the MetersTypeRepository interface
 * for performing CRUD operations related to addresses in the database.
 * The class is annotated with @Repository, indicating that it may be managed
 * by a spring framework container,
 *
 * @author Eugene Kulik
 * @see MetersTypeRepository
 * @see JdbcTemplate
 */
@Repository
@RequiredArgsConstructor
public class JdbcMetersTypeRepository implements MetersTypeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MetersTypeExtractor extractor;


    @Override
    @Loggable
    public Optional<MetersType> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    @Loggable
    public Optional<MetersType> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type
                    WHERE name = ?;
                """, extractor, name));
    }

    @Override
    @Loggable
    public MetersType save(MetersType metersType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name = metersType.getName();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                """
                    INSERT INTO meters.meters_type(id, name)
                    VALUES(nextval('meters.meters_type_sequence'), ?);
                """, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        metersType.setId((Long) keyHolder.getKeys().get("id"));
        return metersType;
    }

    @Override
    @Loggable
    public List<MetersType> findAll() {
        return jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type;
                """,
            new ListExtractor<>(extractor));
    }
}
