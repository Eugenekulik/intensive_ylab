package by.eugenekulik.out.dao.jdbc.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {



    T extractData(ResultSet resultSet) throws SQLException;


}
