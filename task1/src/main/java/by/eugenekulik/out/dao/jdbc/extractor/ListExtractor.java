package by.eugenekulik.out.dao.jdbc.extractor;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ListExtractor<T>} class is a generic implementation of the
 * {@code ResultSetExtractor<List<T>>} interface for extracting a list of
 * objects of type {@code T} from a {@code ResultSet}.
 *
 * @param <T> The type of object to be extracted and included in the list.
 * @author Eugene Kulik
 */
public class ListExtractor<T> implements ResultSetExtractor<List<T>> {

    private final ResultSetExtractor<T> extractor;

    public ListExtractor(ResultSetExtractor<T> extractor) {
        this.extractor = extractor;
    }

    /**
     * Extracts a list of objects of type {@code T} from the provided
     * {@code ResultSet} using the internal item extractor.
     *
     * @param resultSet The {@code ResultSet} containing the data.
     * @return A list of objects of type {@code T} extracted from the
     *         {@code ResultSet}.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException {
        List<T> result = new ArrayList<>();
        while (true) {
            T t = extractor.extractData(resultSet);
            if (t == null) {
                break;
            }
            result.add(t);
        }
        return result;
    }
}
