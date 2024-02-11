package by.eugenekulik.out.dao.jdbc.utils;

import by.eugenekulik.exception.DatabaseInterectionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
@NoArgsConstructor
public class TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger(TransactionManager.class);
    private static final ThreadLocal<Connection> currentConnection = new ThreadLocal<>();
    private ConnectionPool connectionPool;

    @Inject
    public TransactionManager(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static Connection getCurrentConnection() {
        return currentConnection.get();
    }

    public <T> T doInTransaction(TransactionCallback<T> callback) {
        Connection connection = currentConnection.get();
        try {
            if (connection == null) {
                connection = connectionPool.getConnection();
                currentConnection.set(connection);
            }
            connection.setAutoCommit(false);
            T result = callback.execute();
            connection.commit();
            return result;
        } catch (SQLException e) {
            LOGGER.error("Error in transaction", e);
            rollback(connection);
            throw new DatabaseInterectionException("Error in transaction", e);
        } finally {
            closeConnection(connection);
            currentConnection.remove();
        }
    }

    private void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new DatabaseInterectionException("Error rolling back transaction", e);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseInterectionException("Error closing connection", e);
        }
    }
}
