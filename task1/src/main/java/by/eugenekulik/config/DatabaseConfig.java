package by.eugenekulik.config;

import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.DataSource;
import by.eugenekulik.service.MigrationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class DatabaseConfig {


    private static final Logger LOGGER = LogManager.getLogger(DatabaseConfig.class);
    private Properties properties;

    public DatabaseConfig() {

    }

    @PostConstruct
    public void init() {
        properties = new Properties();
        try (InputStream resourceAsStream = getClass().getClassLoader()
            .getResourceAsStream("application.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            LOGGER.error("unable to load configuration file");
            throw new RuntimeException();
        }
        MigrationService migrationService = new MigrationService(getDataSource());
        migrationService.applyChanges();
    }


    @Produces
    public DataSource getDataSource() {
        return new DataSource(properties.getProperty("database.url"),
            properties.getProperty("database.name"),
            properties.getProperty("database.port"),
            properties.getProperty("database.user"),
            properties.getProperty("database.password"),
            properties.getProperty("database.driver"));
    }

    @Produces
    @Named("connectionPool")
    public ConnectionPool getConnectionPool(DataSource dataSource) {
        return new ConnectionPool(dataSource,
            Integer.parseInt(properties.getProperty("database.connection-pool.size", "16")),
            Integer.parseInt(properties.getProperty("database.connection-pool.timeout", "30")));
    }


}
