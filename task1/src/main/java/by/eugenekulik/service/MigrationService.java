package by.eugenekulik.service;

import by.eugenekulik.out.dao.jdbc.utils.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MigrationService {

    private DataSource dataSource;

    public MigrationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void applyChanges(){
        try {
            Connection connection = DriverManager.getConnection(dataSource.getUrl(),
                dataSource.getUser(),
                dataSource.getPassword());
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
