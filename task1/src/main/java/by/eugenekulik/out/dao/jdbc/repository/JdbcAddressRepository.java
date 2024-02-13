package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AddressExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import by.eugenekulik.service.aspect.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * JdbcAddressRepository is a JDBC implementation of the AddressRepository interface
 * for performing CRUD operations related to addresses in the database.
 *
 * The class is annotated with @ApplicationScoped, indicating that it may be managed
 * by a CDI (Contexts and Dependency Injection) container.
 *
 * It also uses the @Loggable annotation to enable logging for the methods in the class.
 *
 * @author Eugene Kulik
 * @see AddressRepository
 * @see JdbcTemplate
 */
@ApplicationScoped
@NoArgsConstructor
public class JdbcAddressRepository implements AddressRepository {

    private JdbcTemplate jdbcTemplate;
    private AddressExtractor extractor = new AddressExtractor();

    @Inject
    public JdbcAddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), pageable.getCount(), pageable.getPage(), pageable.getCount());
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
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor), userId, pageable.getCount(), pageable.getPage(), pageable.getCount());
    }
}
