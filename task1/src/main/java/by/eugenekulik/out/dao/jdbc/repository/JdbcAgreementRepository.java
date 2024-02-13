package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.out.dao.AddressRepository;
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

/**
 * JdbcAgreementRepository is a JDBC implementation of the AgreementRepository interface
 * for performing CRUD operations related to agreements in the database.
 * <p>
 * The class is annotated with @ApplicationScoped, indicating that it may be managed
 * by a CDI (Contexts and Dependency Injection) container.
 * <p>
 * It also uses the @Loggable annotation to enable logging for the methods in the class.
 *
 * @author Eugene Kulik
 * @see AgreementRepository
 * @see JdbcTemplate
 */
@ApplicationScoped
@NoArgsConstructor
public class JdbcAgreementRepository implements AgreementRepository {
    private JdbcTemplate jdbcTemplate;
    private AgreementExtractor extractor = new AgreementExtractor();

    @Inject
    public JdbcAgreementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), pageable.getCount(), pageable.getPage(), pageable.getCount());
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
                    OFFSET ? * ?;
                """, new ListExtractor<>(extractor),
            userId, pageable.getCount(), pageable.getPage(), pageable.getCount());

    }
}
