package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The {@code UserExtractor} class is an implementation of the
 * {@code ResultSetExtractor<User>} interface for extracting
 * a {@code User} object from a {@code ResultSet}.
 *
 * @author  Eugene Kulik
 */
@Component
public class UserExtractor implements ResultSetExtractor<User> {

    /**
     * Extracts a {@code User} object from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return A {@code User} object extracted from the {@code ResultSet},
     *         or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public User extractData(ResultSet resultSet) throws SQLException {
        User user = null;
        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setRole(Role.valueOf(resultSet.getString("role")));
        }
        return user;
    }
}
