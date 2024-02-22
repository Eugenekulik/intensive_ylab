package by.eugenekulik.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class MigrationService {
    private DataSource dataSource;

    public MigrationService(DataSource dataSource) {
        this.dataSource = dataSource;
        applyChanges();
    }

    public void applyChanges() {
        try {
            Connection connection = dataSource.getConnection();
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
