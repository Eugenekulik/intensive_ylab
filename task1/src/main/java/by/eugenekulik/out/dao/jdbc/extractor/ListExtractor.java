package by.eugenekulik.out.dao.jdbc.extractor;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.jdbc.utils.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListExtractor<T> implements ResultSetExtractor<List<T>> {

    private ResultSetExtractor<T> extractor;

    public ListExtractor(ResultSetExtractor<T> extractor) {
        this.extractor = extractor;
    }

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
