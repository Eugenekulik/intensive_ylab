package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MetersTypeExtractor implements ResultSetExtractor<MetersType> {
    @Override
    public MetersType extractData(ResultSet resultSet) throws SQLException {
        MetersType metersType = null;
        if(resultSet.next()){
            metersType = new MetersType();
            metersType.setId(resultSet.getLong("id"));
            metersType.setName(resultSet.getString("name"));
        }
        return metersType;
    }
}
