package by.eugenekulik.out.dao.jdbc.utils;

import by.eugenekulik.exception.DatabaseInterectionException;
import lombok.Getter;

@Getter
public class DataSource {

    private final String url;
    private final String name;
    private final String port;
    private final String user;
    private final String password;

    public DataSource(String url, String name, String port, String user, String password, String driverName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.name = name;
        this.port = port;
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new DatabaseInterectionException("Error to load driver class.", e);
        }
    }

    public String createUrl() {
        return url + ":" + port + "/" + name;
    }
}
