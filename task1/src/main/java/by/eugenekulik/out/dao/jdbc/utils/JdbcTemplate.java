package by.eugenekulik.out.dao.jdbc.utils;


import by.eugenekulik.exception.DatabaseInterectionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

@ApplicationScoped
@NoArgsConstructor
public class JdbcTemplate {

    private static final Logger LOGGER = LogManager.getLogger(JdbcTemplate.class);
    private ConnectionPool connectionPool;


    @Inject
    public JdbcTemplate(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public <T> T query(String sql, ResultSetExtractor<T> resultSetExtractor, Object... params) {
        Connection connection = TransactionManager.getCurrentConnection();
        if (connection == null) {
            connection = connectionPool.getConnection();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSetExtractor.extractData(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseInterectionException("Error executing query", e);
        } finally {
            if (TransactionManager.getCurrentConnection() == null) {
                closeConnection(connection);
            }
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("An error occurred while closing connection");
            throw new DatabaseInterectionException("Error closing connection", e);
        }
    }

    public int update(String sql, Object... params) {
        Connection connection = TransactionManager.getCurrentConnection();
        if (connection == null) {
            connection = connectionPool.getConnection();
        }
        try (PreparedStatement preparedStatement =
                 connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseInterectionException("Error executing update", e);
        } finally {
            if (TransactionManager.getCurrentConnection() == null) {
                closeConnection(connection);
            }
        }
    }


}
