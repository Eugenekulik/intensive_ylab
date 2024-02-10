package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AgreementExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcAgreementRepository implements AgreementRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AgreementExtractor extractor;

    public JdbcAgreementRepository(JdbcTemplate jdbcTemplate) {
        this.extractor = new AgreementExtractor();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Agreement> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT agreement.id, agreement.user_id, agreement.address_id
                    FROM agreement
                    WHERE agreement.id = ?;
                """, extractor, id));
    }

    @Override
    public Agreement save(Agreement agreement) {
        Long id = jdbcTemplate.query("SELECT nextval ('agreement_sequence');",
            rs -> {
                rs.next();
                return rs.getLong(1);
            });
        agreement.setId(id);
        jdbcTemplate.update(
            """
                    INSERT INTO agreement(id, user_id, address_id)
                    VALUES(?, ?, ?);
                """, agreement.getId(), agreement.getUserId(), agreement.getAddressId());
        return agreement;
    }

    @Override
    public List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId) {
        return jdbcTemplate.query(
            """
                    SELECT agreement.id, agreement.user_id, agreement.address_id
                    FROM agreement
                    WHERE agreement.user_id = ? and agreement.address_id = ?;
                """, new ListExtractor<>(extractor), userId, addressId);
    }

    @Override
    public List<Agreement> getPage(int page, int count) {
        return jdbcTemplate.query(
            """
                    SELECT agreement.id, agreement.user_id, agreement.address_id
                    FROM agreement
                    ORDER BY agreement.id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor),count, page, count);
    }

    @Override
    public List<Agreement> findByUserId(Long userId) {
        return jdbcTemplate.query(
            """
                    SELECT agreement.id, agreement.user_id, agreement.address_id
                    FROM agreement
                    WHERE agreement.user_id = ?;
                """, new ListExtractor<>(extractor), userId);

    }
}
