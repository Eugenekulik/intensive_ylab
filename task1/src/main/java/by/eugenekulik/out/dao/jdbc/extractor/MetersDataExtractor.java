package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetersDataExtractor implements ResultSetExtractor<MetersData> {
    @Override
    public MetersData extractData(ResultSet resultSet) throws SQLException {
        MetersData metersData = null;
        if(resultSet.next()){
            metersData = new MetersData();
            metersData.setId(resultSet.getLong("id"));
            metersData.setAgreementId(resultSet.getLong("agreement_id"));
            metersData.setPlacedAt(resultSet.getTimestamp("placed_at")
                .toLocalDateTime());
            metersData.setMetersTypeId(resultSet.getLong("meters_type_id"));
            metersData.setValue(resultSet.getDouble("value"));
        }
        return metersData;
    }
}
