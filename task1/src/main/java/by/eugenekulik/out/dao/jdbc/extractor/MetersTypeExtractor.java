package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.MetersType;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code MetersTypeExtractor} class is an implementation of the
 * {@code ResultSetExtractor<MetersType>} interface for extracting
 * a {@code MetersType} object from a {@code ResultSet}.
 *
 * @author  Eugene Kulik
 */
@Component
public class MetersTypeExtractor implements ResultSetExtractor<MetersType> {

    /**
     * Extracts a {@code MetersType} object from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return A {@code MetersType} object extracted from the {@code ResultSet},
     *         or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public MetersType extractData(ResultSet resultSet) throws SQLException {
        MetersType metersType = null;
        if (resultSet.next()) {
            metersType = new MetersType();
            metersType.setId(resultSet.getLong("id"));
            metersType.setName(resultSet.getString("name"));
        }
        return metersType;
    }
}
