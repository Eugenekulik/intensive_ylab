package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.extractor.AddressExtractor;
import by.eugenekulik.out.dao.jdbc.extractor.ListExtractor;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcAddressRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AddressExtractor extractor;

    public JdbcAddressRepository(JdbcTemplate jdbcTemplate) {
        this.extractor = new AddressExtractor();
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<Address> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate
            .query("""
                           SELECT id, region, district, city, street, house, apartment
                           FROM address
                           WHERE id = ?;
                       """, extractor, id));
    }

    @Override
    public boolean isPresent(Address address) {
        return jdbcTemplate.query(
            """
                    SELECT count(*)
                    FROM address
                    WHERE address.region = ? and address.district = ? and address.city = ? and
                    address.street = ? and address.house = ? and address.apartment = ?;
                """,
            rs -> {
                rs.next();
                int count = rs.getInt("count");
                return count != 0;
            },
            address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouse(), address.getApartment());
    }

    @Override
    public Address save(Address address) {
        Long id = jdbcTemplate.query("SELECT nextval ('address_sequence');",
            rs -> {
                rs.next();
                return rs.getLong(1);
            });
        address.setId(id);
        jdbcTemplate.update(
            """
                    INSERT INTO address(id, region, district, city, street, house, apartment)
                    VALUES(?, ?, ?, ?, ?, ?, ?);
                """,
            address.getId(), address.getRegion(), address.getDistrict(), address.getCity(),
            address.getStreet(), address.getHouse(), address.getApartment()
            );
        return address;
    }

    @Override
    public List<Address> getPage(int page, int count) {
        return jdbcTemplate.query(
            """
                    SELECT address.id, address.region, address.district,
                    address.city, address.street, address.house, address.apartment
                    FROM address
                    ORDER BY address.id
                    LIMIT ?
                    OFFSET ?*?;
                """,
            new ListExtractor<>(extractor) ,count, page, count);
    }
}
