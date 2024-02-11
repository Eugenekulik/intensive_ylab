package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressExtractor implements ResultSetExtractor<Address> {
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
