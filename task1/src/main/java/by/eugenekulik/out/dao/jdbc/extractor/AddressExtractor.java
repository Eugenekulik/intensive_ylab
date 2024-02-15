package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AddressExtractor} class is an implementation of the
 * {@code ResultSetExtractor<Address>} interface for extracting
 * an {@code Address} object from a {@code ResultSet}.
 *
 * @author Eugene Kulik
 */
public class AddressExtractor implements ResultSetExtractor<Address> {

    /**
     * Extracts an {@code Address} object from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return An {@code Address} object extracted from the {@code ResultSet},
     *         or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public Address extractData(ResultSet resultSet) throws SQLException {
        Address address = null;
        if (resultSet.next()) {
            address = new Address();
            address.setId(resultSet.getLong("id"));
            address.setRegion(resultSet.getString("region"));
            address.setDistrict(resultSet.getString("district"));
            address.setCity(resultSet.getString("city"));
            address.setStreet(resultSet.getString("street"));
            address.setHouse(resultSet.getString("house"));
            address.setApartment(resultSet.getString("apartment"));
        }
        return address;
    }
}
