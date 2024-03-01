package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersDataExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * JdbcMetersDataRepository is a JDBC implementation of the MetersDataRepository interface
 * for performing CRUD operations related to metersData in the database.
 * The class is annotated with @Repository, indicating that it may be managed
 * by a spring framework container.
 *
 * @author Eugene Kulik
 * @see MetersDataRepository
 * @see JdbcTemplate
 */
@Repository
public class JdbcMetersDataRepository implements MetersDataRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MetersDataExtractor extractor;

    public JdbcMetersDataRepository(JdbcTemplate jdbcTemplate, MetersDataExtractor extractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
    }


    @Override
    @Loggable
    public Optional<MetersData> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    @Loggable
    public MetersData save(MetersData metersData) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Long metersTypeId = metersData.getMetersTypeId();
        Long agreementId = metersData.getAgreementId();
        Double value = metersData.getValue();
        LocalDateTime placedAt = metersData.getPlacedAt();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("""
                    INSERT INTO meters.meters_data(id,meters_type_id, agreement_id, value, placed_at)
                    VALUES(nextval('meters.meters_data_sequence'), ?, ?, ?, ?);
                """, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, metersTypeId);
            ps.setLong(2, agreementId);
            ps.setDouble(3, value);
            ps.setTimestamp(4, Timestamp.from(placedAt.toInstant(ZoneOffset.UTC)));
            return ps;
        }, keyHolder);
        metersData.setId((Long) keyHolder.getKeys().get("id"));
        return metersData;
    }

    @Override
    @Loggable
    public Optional<MetersData> findByAgreementAndTypeAndMonth(Long agreementId,
                                                               Long metersTypeId,
                                                               LocalDate placedAt) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE agreement_id = ? and meters_type_id = ?
                    and EXTRACT(MONTH FROM placed_at) = EXTRACT(MONTH FROM ?)
                    and EXTRACT(YEAR FROM placed_at) = EXTRACT(YEAR FROM ?);
                """, extractor, agreementId, metersTypeId, placedAt, placedAt));
    }

    @Override
    @Loggable
    public List<MetersData> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?;
                """,
            new ListExtractor<>(extractor), pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());

    }

    @Override
    @Loggable
    public List<MetersData> getPageByAgreement(Long agreementId, int page, int count) {
        return jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE agreement_id = ?
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), agreementId, count, page, count);

    }

    @Override
    @Loggable
    public Optional<MetersData> findLastByAgreementAndType(Long agreementId, Long metersTypeId) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE agreement_id = ? and meters_type_id = ?
                    ORDER BY placed_at desc
                    LIMIT 1;
                """, extractor, agreementId, metersTypeId));
    }

    @Override
    @Loggable
    public List<MetersData> findByAgreementAndType(long agreementId, Long meterTypeId, Pageable pageable) {
        return jdbcTemplate.query("""
                        SELECT id, meters_type_id, agreement_id, value, placed_at
                        FROM meters.meters_data
                        WHERE agreement_id = ? and meters_type_id = ?
                        ORDER BY placed_at desc
                        LIMIT ?
                        OFFSET ?;
                """, new ListExtractor<>(extractor),
            agreementId, meterTypeId, pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());
    }
}
