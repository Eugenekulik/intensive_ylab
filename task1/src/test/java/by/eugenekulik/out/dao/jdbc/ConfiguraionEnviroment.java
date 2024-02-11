package by.eugenekulik.out.dao.jdbc;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ConfiguraionEnviroment {
    protected static PostgresTestContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}
