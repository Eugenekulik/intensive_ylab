package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AddressExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.starter.logging.annotation.Loggable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        jdbcTemplate.update(
            """
                    INSERT INTO meters.address(id,region, district, city, street, house, apartment)
                    VALUES(nextval('meters.address_sequence'),?, ?, ?, ?, ?, ?);
                """,
            address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouse(), address.getApartment()
        );
        address = jdbcTemplate.query("""
                        SELECT id, region, district, city, street, house, apartment
                        FROM meters.address
                        where region = ? and district = ? and city = ?
                        and street = ? and house = ? and apartment = ?;
                """, extractor, address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouse(), address.getApartment());
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
