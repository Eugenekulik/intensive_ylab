package by.eugenekulik;

import by.eugenekulik.out.dao.jdbc.utils.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private DataSource dataSource;
    private final static String IMAGE_VERSION = "postgres:15-alpine";
    private static PostgresTestContainer container;

    private PostgresTestContainer(){
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer getInstance(){
        if(container == null){
            container = new PostgresTestContainer().withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        dataSource = new DataSource(
            "jdbc:postgresql://localhost:" +
                getFirstMappedPort().toString() + "/" + getDatabaseName(),
            getUsername(),
            getPassword(),
            "org.postgresql.Driver");
        migration();

    }

    private void migration() {
        try{
            Connection connection = DriverManager.getConnection(dataSource.getUrl(),
                dataSource.getUser(),
                dataSource.getPassword());
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource(){
        return dataSource;
    }

    @Override
    public void stop() {}
}
