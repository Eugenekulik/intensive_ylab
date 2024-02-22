package by.eugenekulik;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("by.eugenekulik")
public class TestConfig {

    @Bean
    public PostgresTestContainer postgresTestContainer(){
        return new PostgresTestContainer();
    }
    @Bean
    public DataSource dataSource(PostgresTestContainer postgresTestContainer){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(postgresTestContainer.getJdbcUrl());
        dataSource.setUsername(postgresTestContainer.getUsername());
        dataSource.setPassword(postgresTestContainer.getPassword());
        dataSource.setDriverClassName(postgresTestContainer.getDriverClassName());
        return dataSource;
    }
}
