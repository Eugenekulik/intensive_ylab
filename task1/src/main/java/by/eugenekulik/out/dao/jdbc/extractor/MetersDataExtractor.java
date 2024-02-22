package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.MetersData;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code MetersDataExtractor} class is an implementation of the
 * {@code ResultSetExtractor<MetersData>} interface for extracting
 * a {@code MetersData} object from a {@code ResultSet}.
 *
 * @author  Eugene Kulik
 */
@Component
public class MetersDataExtractor implements ResultSetExtractor<MetersData> {

    /**
     * Extracts a {@code MetersData} object from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return A {@code MetersData} object extracted from the {@code ResultSet},
     *         or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public MetersData extractData(ResultSet resultSet) throws SQLException {
        MetersData metersData = null;
        if (resultSet.next()) {
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
