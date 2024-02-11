package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgreementExtractor implements ResultSetExtractor<Agreement> {
    @Override
    public Agreement extractData(ResultSet resultSet) throws SQLException {
        Agreement agreement = null;
        if (resultSet.next()) {
            agreement = new Agreement();
            agreement.setId(resultSet.getLong("id"));
            agreement.setUserId(resultSet.getLong("user_id"));
            agreement.setAddressId(resultSet.getLong("address_id"));
        }
        return agreement;
    }
}
