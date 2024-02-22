package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AgreementExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.service.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JdbcAgreementRepository is a JDBC implementation of the AgreementRepository interface
 * for performing CRUD operations related to agreements in the database.
 * The class is annotated with @Repository, indicating that it may be managed
 * by a spring framework container.
 *
 * @author Eugene Kulik
 * @see AgreementRepository
 * @see JdbcTemplate
 */
@Repository
@Slf4j
public class JdbcAgreementRepository implements AgreementRepository {
    private final JdbcTemplate jdbcTemplate;
    private final AgreementExtractor extractor;

    public JdbcAgreementRepository(JdbcTemplate jdbcTemplate, AgreementExtractor extractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
    }

    @Override
    @Loggable
    public Optional<Agreement> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    @Loggable
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
    @Loggable
    public List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE user_id = ? and address_id = ?;
                """, new ListExtractor<>(extractor), userId, addressId);
    }

    @Override
    @Loggable
    public List<Agreement> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?;
                """,
            new ListExtractor<>(extractor), pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());
    }

    @Override
    @Loggable
    public List<Agreement> findByUserId(Long userId, Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, user_id, address_id
                    FROM meters.agreement
                    WHERE user_id = ?
                    LIMIT ?
                    OFFSET ?;
                """, new ListExtractor<>(extractor),
            userId, pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());

    }
}
