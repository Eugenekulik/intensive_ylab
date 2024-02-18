package by.eugenekulik;

import by.eugenekulik.service.MigrationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private final static String IMAGE_VERSION = "postgres:15-alpine";


    public PostgresTestContainer(){
        super(IMAGE_VERSION);
    }




    @PostConstruct
    public void init(){
        start();
    }

    @PreDestroy
    public void destroy(){
        stop();
    }


}
