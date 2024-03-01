package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Agreement;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AgreementExtractor} class is an implementation of the
 * {@code ResultSetExtractor<Agreement>} interface for extracting
 * an {@code Agreement} object from a {@code ResultSet}.
 *
 * @Eugene Kulik
 */
@Component
public class AgreementExtractor implements ResultSetExtractor<Agreement> {

    /**
     * Extracts an {@code Agreement} object from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return An {@code Agreement} object extracted from the {@code ResultSet},
     *         or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public Agreement extractData(ResultSet resultSet) throws SQLException {
        Agreement agreement = null;
        if (resultSet.next()) {
            agreement = new Agreement();
            agreement.setId(resultSet.getLong("id"));
            agreement.setUserId(resultSet.getLong("user_id"));
            agreement.setAddressId(resultSet.getLong("address_id"));
            agreement.setActive(resultSet.getBoolean("active"));
        }
        return agreement;
    }
}
