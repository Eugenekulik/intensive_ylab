package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.MetersDataExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JdbcMetersDataRepository implements MetersDataRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MetersDataExtractor extractor;

    public JdbcMetersDataRepository(JdbcTemplate jdbcTemplate) {
        extractor = new MetersDataExtractor();
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<MetersData> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT meters_data.id, meters_data.meters_type_id, meters_data.agreement_id,
                    meters_data.value, meters_data.placed_at
                    FROM meters_data
                    WHERE meters_data.id = ?;
                """, extractor, id));
    }

    @Override
    public MetersData save(MetersData metersData) {
        Long id = jdbcTemplate.query("SELECT nextval ('meters_data_sequence');",
            rs -> {
                rs.next();
                return rs.getLong(1);
            });
        metersData.setId(id);
        jdbcTemplate.update(
            """
                    INSERT INTO meters_data(id, meters_type_id, agreement_id, value, placed_at)
                    VALUES(?, ?, ?, ?, ?);
                """,
            metersData.getId(), metersData.getMetersTypeId(), metersData.getAgreementId(),
            metersData.getValue(), metersData.getPlacedAt());
        return metersData;
    }

    @Override
    public Optional<MetersData> findByAgreementAndTypeAndMonth(Long agreementId,
                                                               Long metersTypeId,
                                                               LocalDate placedAt) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT meters_data.id, meters_data.meters_type_id, meters_data.agreement_id,
                    meters_data.value, meters_data.placed_at
                    FROM meters_data
                    WHERE meters_data.agreement_id = ? and meters_data.meters_type_id = ?
                    and EXTRACT(MONTH FROM meters_data.placed_at) = EXTRACT(MONTH FROM ?)
                    and EXTRACT(YEAR FROM meters_data.placed_at) = EXTRACT(YEAR FROM ?);
                """, extractor, agreementId, metersTypeId, placedAt, placedAt));
    }

    @Override
    public List<MetersData> getPage(int page, int count) {
        return jdbcTemplate.query(
            """
                    SELECT meters_data.id, meters_data.meters_type_id, meters_data.agreement_id,
                    meters_data.value, meters_data.placed_at
                    FROM meters_data
                    ORDER BY meters_data.id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor) ,count, page, count);

    }

    @Override
    public List<MetersData> getPageByAgreement(Long agreementId, int page, int count) {
        return jdbcTemplate.query(
            """
                    SELECT meters_data.id, meters_data.meters_type_id, meters_data.agreement_id,
                    meters_data.value, meters_data.placed_at
                    FROM meters_data
                    WHERE meters_data.agreement_id = ?
                    ORDER BY meters_data.id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), agreementId, count, page, count);

    }

    @Override
    public Optional<MetersData> findLastByAgreementAndType(Long agreementId, Long metersTypeId) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT meters_data.id, meters_data.meters_type_id, meters_data.agreement_id,
                    meters_data.value, meters_data.placed_at
                    FROM meters_data
                    WHERE meters_data.agreement_id = ? and meters_data.meters_type_id = ?
                    ORDER BY meters_data.placed_at desc
                    LIMIT 1;
                """, extractor, agreementId, metersTypeId));
    }
}
