package by.eugenekulik.out.dao.jdbc.utils;

import by.eugenekulik.exception.DatabaseInterectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private static final Logger LOGGER = LogManager.getLogger(TransactionManager.class);
    private final ConnectionPool connectionPool;
    private static ThreadLocal<Connection> currentConnection = new ThreadLocal<>();

    public TransactionManager(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
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


    public static Connection getCurrentConnection() {
        return currentConnection.get();
    }

    private void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error rolling back transaction", e);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing connection", e);
        }
    }
}
