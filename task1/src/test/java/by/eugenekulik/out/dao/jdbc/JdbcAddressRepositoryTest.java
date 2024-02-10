package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.jdbc.repository.JdbcAddressRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class JdbcAddressRepositoryTest {

    private static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static JdbcAddressRepository addressRepository;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
        DataSource dataSource = new DataSource("jdbc:postgresql://localhost:" +
            postgreSQLContainer.getFirstMappedPort() + "/test", "test",
            "test", "org.postgresql.Driver");
        ConnectionPool connectionPool = ConnectionPool
            .createConnectionPool(dataSource, 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        addressRepository = new JdbcAddressRepository(jdbcTemplate);
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:" +
                    postgreSQLContainer.getFirstMappedPort() + "/test", "test",
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
    void testSave_shouldSaveAndReturnAddress(){
        Address address = Address.builder()
            .region("Grodno region")
            .district("Volkovysk district")
            .city("Volkovysk")
            .street("Gorbatovo")
            .house("9")
            .apartment("2")
            .build();

        assertThat(addressRepository.save(address))
            .isNotNull()
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("region", "Grodno region");
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfAddress_whenAddressExists(){
        assertTrue(addressRepository.findById(1L).isPresent());
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenAddressNotExists(){
        assertTrue(addressRepository.findById(1000L).isEmpty());
    }

    @Test
    @Order(2)
    void testIsPresent_shouldReturnTrue_whenExistAddressWithTheSameValuesOfField(){
        Address address = Address.builder()
            .region("Grodno region")
            .district("Volkovysk district")
            .city("Volkovysk")
            .street("Gorbatovo")
            .house("9")
            .apartment("2")
            .build();

        assertTrue(addressRepository.isPresent(address));
    }

    @Test
    @Order(2)
    void testIsPresent_shouldReturnFalse_whenNotExistAddressWithTheSameValuesOfField(){
        Address address = Address.builder()
            .region("minsk")
            .district("minsk")
            .city("minsk")
            .street("nezavisimosti")
            .house("61")
            .apartment("42")
            .build();

        assertFalse(addressRepository.isPresent(address));
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfAddresses_whenPageAndCountValid(){
        int page = 0;
        int count = 2;

        List<Address> result = addressRepository.getPage(page, count);

        assertThat(result)
            .hasSize(2)
            .anyMatch(address -> address.getRegion().equals("Grodno region"));
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 4;

        List<Address> result = addressRepository.getPage(page, count);

        assertThat(result).isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(DatabaseInterectionException.class, ()->addressRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(DatabaseInterectionException.class, ()->addressRepository.getPage(page, count));
    }

}