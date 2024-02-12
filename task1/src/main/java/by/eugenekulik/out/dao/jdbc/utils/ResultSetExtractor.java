package by.eugenekulik.out.dao.jdbc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code ResultSetExtractor} interface is a generic interface that defines
 * a method for extracting data from a {@code ResultSet}.
 *
 * @param <T> The type of object to be extracted from the {@code ResultSet}.
 */
public interface ResultSetExtractor<T> {

    /**
     * Extracts data from the provided {@code ResultSet}.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return An object of type {@code T} extracted from the {@code ResultSet}.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    T extractData(ResultSet resultSet) throws SQLException;
}