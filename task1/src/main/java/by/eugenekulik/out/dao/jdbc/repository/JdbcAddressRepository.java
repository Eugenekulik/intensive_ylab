package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AddressExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcAddressRepository is a JDBC implementation of the AddressRepository interface
 * for performing CRUD operations related to addresses in the database.
 * The class is annotated with @Repository, indicating that it may be managed
 * by spring framework container.
 *
 * @author Eugene Kulik
 * @see AddressRepository
 * @see JdbcTemplate
 */
@Repository
public class JdbcAddressRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AddressExtractor extractor;

    public JdbcAddressRepository(JdbcTemplate jdbcTemplate, AddressExtractor extractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
    }


    @Override
    @Loggable
    public Optional<Address> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate
            .query("""
                    SELECT id, region, district, city, street, house, apartment
                    FROM meters.address
                    WHERE id = ?;
                """, extractor, id));
    }

    @Override
    @Loggable
    public Address save(Address address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String region = address.getRegion();
        String district = address.getDistrict();
        String city = address.getCity();
        String street = address.getStreet();
        String house = address.getHouse();
        String apartment = address.getApartment();
        jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    """
                            INSERT INTO meters.address(id,region, district, city, street, house, apartment)
                            VALUES(nextval('meters.address_sequence'),?, ?, ?, ?, ?, ?);
                        """, Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, region);
                ps.setString(2, district);
                ps.setString(3, city);
                ps.setString(4, street);
                ps.setString(5, house);
                ps.setString(6, apartment);
                return ps;
            }, keyHolder);
        address.setId((Long) keyHolder.getKeys().get("id"));
        return address;
    }

    @Override
    @Loggable
    public List<Address> getPage(Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, region, district, city, street, house, apartment
                    FROM meters.address
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?;
                """,
            new ListExtractor<>(extractor), pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());
    }

    @Override
    @Loggable
    public List<Address> findByUserId(Long userId, Pageable pageable) {
        return jdbcTemplate.query(
            """
                    SELECT id, region, district, city, street, house, apartment
                    FROM meters.address left join meters.agreement
                    ON meters.address.id = meters.agreement.address_id
                    WHERE user_id = ?
                    ORDER BY id
                    LIMIT ?
                    OFFSET ?;
                """,
            new ListExtractor<>(extractor), userId, pageable.getPageSize(),
            pageable.getPageNumber() * pageable.getPageSize());
    }
}
