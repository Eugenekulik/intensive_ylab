package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.jdbc.repository.JdbcAgreementRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public class JdbcAgreementRepositoryTest {


    private static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    private static JdbcAgreementRepository agreementRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer.start();
        DataSource dataSource = new DataSource("jdbc:postgresql://localhost:"+
            postgreSQLContainer.getFirstMappedPort() + "/test","test",
            "test", "org.postgresql.Driver");
        ConnectionPool connectionPool = ConnectionPool
            .createConnectionPool(dataSource, 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        agreementRepository = new JdbcAgreementRepository(jdbcTemplate);
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
    void testSave_shouldReturnSavedAgreement(){
        Agreement agreement = Agreement
            .builder()
            .addressId(1L)
            .userId(1L)
            .build();

        assertThat(agreementRepository.save(agreement))
            .isNotNull()
            .hasFieldOrPropertyWithValue("addressId", 1L)
            .hasFieldOrPropertyWithValue("userId", 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfAgreement_whenAgreementExists(){
        long id = 1L;

        assertThat(agreementRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("userId", 2L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenAgreementNotExists(){
        long id = 1000L;

        assertThat(agreementRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByUserIdAndAddressId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 2L;
        long addressId = 1L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    void testFindByUserIdAndAddressId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 3L;
        long addressId = 3L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByUserId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 2L;

        assertThat(agreementRepository.findByUserId(userId))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    void testFindByUserId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 3L;

        assertThat(agreementRepository.findByUserId(userId))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfAgreement_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        assertThat(agreementRepository.getPage(page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("addressId", 1L);
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 4;

        assertThat(agreementRepository.getPage(page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(DatabaseInterectionException.class, ()->agreementRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(DatabaseInterectionException.class, ()->agreementRepository.getPage(page, count));
    }

}
