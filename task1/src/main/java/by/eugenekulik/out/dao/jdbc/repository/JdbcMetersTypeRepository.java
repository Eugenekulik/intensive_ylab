package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersTypeExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import by.eugenekulik.service.aspect.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
@Loggable
public class JdbcMetersTypeRepository implements MetersTypeRepository {

    private JdbcTemplate jdbcTemplate;
    private MetersTypeExtractor extractor = new MetersTypeExtractor();

    @Inject
    public JdbcMetersTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MetersType> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    public Optional<MetersType> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type
                    WHERE name = ?;
                """, extractor, name));
    }

    @Override
    public MetersType save(MetersType metersType) {
        jdbcTemplate.update(
            """
                    INSERT INTO meters.meters_type(id, name)
                    VALUES(nextval('meters.meters_type_sequence'), ?);
                """,
            metersType.getName());
        metersType = jdbcTemplate.query("""
                    SELECT id, name
                    FROM meters.meters_type
                    WHERE name = ?;
            """, extractor, metersType.getName());
        return metersType;
    }

    @Override
    public List<MetersType> findAll() {
        return jdbcTemplate.query(
            """
                    SELECT id, name
                    FROM meters.meters_type;
                """,
            new ListExtractor<>(extractor));
    }
}
