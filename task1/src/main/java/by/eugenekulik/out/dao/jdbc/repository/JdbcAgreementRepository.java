package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AgreementExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
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
public class JdbcAgreementRepository implements AgreementRepository {
    private JdbcTemplate jdbcTemplate;
    private AgreementExtractor extractor = new AgreementExtractor();

    @Inject
    public JdbcAgreementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Agreement> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    public Agreement save(Agreement agreement) {
        jdbcTemplate.update(
            """
                    INSERT INTO meters.agreement(id,user_id, address_id)
                    VALUES(nextval('meters.agreement_sequence'), ?, ?);
                """, agreement.getUserId(), agreement.getAddressId());
        agreement = jdbcTemplate.query("""
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE user_id = ? and address_id = ?;
            """, extractor, agreement.getUserId(), agreement.getAddressId());
        return agreement;
    }

    @Override
    public List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE user_id = ? and address_id = ?;
                """, new ListExtractor<>(extractor), userId, addressId);
    }

    @Override
    public List<Agreement> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), pageable.getCount(), pageable.getPage(), pageable.getCount());
    }

    @Override
    public List<Agreement> findByUserId(Long userId, Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE user_id = ?
                    LIMIT ?
                    OFFSET ? * ?;
                """, new ListExtractor<>(extractor),
            userId, pageable.getCount(), pageable.getPage(), pageable.getCount());

    }
}
