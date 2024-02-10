package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersTypeExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcMetersTypeRepository implements MetersTypeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MetersTypeExtractor extractor;

    public JdbcMetersTypeRepository(JdbcTemplate jdbcTemplate) {
        this.extractor = new MetersTypeExtractor();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MetersType> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT meters_type.id, meters_type.name
                    FROM meters_type
                    WHERE meters_type.id = ?;
                """, extractor, id));
    }

    @Override
    public Optional<MetersType> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT meters_type.id, meters_type.name
                    FROM meters_type
                    WHERE meters_type.name = ?;
                """, extractor, name));
    }

    @Override
    public MetersType save(MetersType metersType) {
        Long id = jdbcTemplate.query("SELECT nextval ('meters_type_sequence');",
            rs -> {
                rs.next();
                return rs.getLong(1);
            });
        metersType.setId(id);
        jdbcTemplate.update(
            """
                    INSERT INTO meters_type(id, name)
                    VALUES(?, ?);
                """,
            metersType.getId(), metersType.getName());
        return metersType;
    }

    @Override
    public List<MetersType> findAll() {
        return jdbcTemplate.query(
            """
                    SELECT meters_type.id, meters_type.name
                    FROM meters_type;
                """,
            new ListExtractor<>(extractor));
    }
}
