package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersDataExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import by.eugenekulik.service.aspect.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
@Loggable
public class JdbcMetersDataRepository implements MetersDataRepository {
    private JdbcTemplate jdbcTemplate;
    private MetersDataExtractor extractor = new MetersDataExtractor();

    @Inject
    public JdbcMetersDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<MetersData> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    public MetersData save(MetersData metersData) {
        jdbcTemplate.update(
            """
                    INSERT INTO meters.meters_data(id,meters_type_id, agreement_id, value, placed_at)
                    VALUES(nextval('meters.meters_data_sequence'), ?, ?, ?, ?);
                """,
            metersData.getMetersTypeId(), metersData.getAgreementId(),
            metersData.getValue(), metersData.getPlacedAt());
        metersData = jdbcTemplate.query("""
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    WHERE placed_at = ?;
            """, extractor, metersData.getPlacedAt());
        return metersData;
    }

    @Override
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
    public List<MetersData> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, meters_type_id, agreement_id, value, placed_at
                    FROM meters.meters_data
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), pageable.getCount(), pageable.getPage(), pageable.getCount());

    }

    @Override
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
    public List<MetersData> findByAgreementAndType(long agreementId, Long meterTypeId, Pageable pageable) {
        return jdbcTemplate.query("""
                        SELECT id, meters_type_id, agreement_id, value, placed_at
                        FROM meters.meters_data
                        WHERE agreement_id = ? and meters_type_id = ?
                        ORDER BY placed_at desc
                        LIMIN ?
                        OFFSET ? * ?;
                """, new ListExtractor<>(extractor),
            agreementId, meterTypeId, pageable.getCount(), pageable.getPage(), pageable.getCount());
    }
}
