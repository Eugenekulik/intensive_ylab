package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.jdbc.repository.JdbcMetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.DataSource;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class JdbcMetersTypeRepositoryTest {

    private static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static JdbcMetersTypeRepository metersTypeRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer.start();
        DataSource dataSource = new DataSource("jdbc:postgresql://localhost:"+
            postgreSQLContainer.getFirstMappedPort() + "/test","test",
            "test", "org.postgresql.Driver");
        ConnectionPool connectionPool = ConnectionPool
            .createConnectionPool(dataSource, 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        metersTypeRepository = new JdbcMetersTypeRepository(jdbcTemplate);
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:"+
                    postgreSQLContainer.getFirstMappedPort() + "/test","test",
                "test");
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
                new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(1)
    void testSave_shouldReturnSavedMetersType(){
        MetersType metersType = MetersType.builder()
            .name("electricity")
            .build();

        assertThat(metersTypeRepository.save(metersType))
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("name", "electricity");
    }


    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        long id = 1L;

        assertThat(metersTypeRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        long id = 1000L;

        assertThat(metersTypeRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByName_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        String name = "warm_water";

        assertThat(metersTypeRepository.findByName(name))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    @Order(2)
    void testFindByName_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        String name = "gas";

        assertThat(metersTypeRepository.findByName(name))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindAll_shouldReturnListOfAllMetersData(){

        assertThat(metersTypeRepository.findAll())
            .hasSize(4)
            .anyMatch(metersType -> metersType.getName().equals("warm_water"))
            .anyMatch(metersType -> metersType.getName().equals("cold_water"))
            .anyMatch(metersType -> metersType.getName().equals("heating"))
            .anyMatch(metersType -> metersType.getName().equals("electricity"));
    }

}