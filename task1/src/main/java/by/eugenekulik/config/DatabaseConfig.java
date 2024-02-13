package by.eugenekulik.config;

import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.DataSource;
import by.eugenekulik.service.MigrationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
@Slf4j
@NoArgsConstructor
public class DatabaseConfig {


    private Properties properties;


    @PostConstruct
    public void init() {
        properties = new Properties();
        String currentDirectory = System.getProperty("java.class.path");
        String filePath = currentDirectory + "/application.properties";
        try (InputStream resourceAsStream = getClass().getClassLoader()
            .getResourceAsStream("application.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            log.error("unable to load configuration file");
        }
        Map<String, String> getenv = System.getenv();
        getenv.entrySet().forEach(e-> properties.put(e.getKey()
            .toLowerCase().replace('_','.'), e.getValue()));
        MigrationService migrationService = new MigrationService(getDataSource());
        migrationService.applyChanges();
    }


    @Produces
    public DataSource getDataSource() {
        DataSource dataSource = new DataSource(properties.getProperty("database.url"),
            properties.getProperty("database.user"),
            properties.getProperty("database.password"),
            properties.getProperty("database.driver"));
        return dataSource;
    }

    @Produces
    @Named("connectionPool")
    public ConnectionPool getConnectionPool(DataSource dataSource) {
        return new ConnectionPool(dataSource,
            Integer.parseInt(properties.getProperty("database.connection-pool.size", "16")),
            Integer.parseInt(properties.getProperty("database.connection-pool.timeout", "30")));
    }


}
