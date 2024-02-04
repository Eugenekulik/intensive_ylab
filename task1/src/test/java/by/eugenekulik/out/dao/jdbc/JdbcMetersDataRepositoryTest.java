package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.jdbc.repository.JdbcMetersDataRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class JdbcMetersDataRepositoryTest {

    private static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private static JdbcMetersDataRepository metersDataRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer.start();
        DataSource dataSource = new DataSource("jdbc:postgresql://localhost:"+
            postgreSQLContainer.getFirstMappedPort() + "/test","test",
            "test", "org.postgresql.Driver");
        ConnectionPool connectionPool = ConnectionPool
            .createConnectionPool(dataSource, 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        metersDataRepository = new JdbcMetersDataRepository(jdbcTemplate);
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
    void testSave_shouldReturnSavedMetersData(){
        MetersData metersData = MetersData.builder()
            .agreementId(1L)
            .placedAt(LocalDateTime.now())
            .value(100.0)
            .metersTypeId(1L)
            .build();

        assertThat(metersDataRepository.save(metersData))
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfMetersData_whenIdIsValid(){
        long id = 1L;

        assertThat(metersDataRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0)
            .hasFieldOrPropertyWithValue("metersTypeId", 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenIdIsNotValid(){
        long id = 1000L;

        assertThat(metersDataRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByAgreementAndTypeAndMonth_shouldReturnOptionalOfMetersData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;
        LocalDate localDate = LocalDate.of(2024,2,4);

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId , localDate))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testFindByAgreementAndTypeAndMonth_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;
        LocalDate localDate = LocalDate.now();

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId, localDate))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindLastByAgreementAndType_shouldReturnOptionalOfMeterData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
    @Order(2)
    void testFindLastByAgreementAndType_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnListOfMetersData_whenArgumentAreValid(){
        int page = 0;
        int count = 1;
        long agreementId = 1L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnEmptyList_whenAgreementNotValid(){
        int page = 0;
        int count = 1;
        long agreementId = 2L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 4;
        long agreementId = 1L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfMetersData_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        assertThat(metersDataRepository.getPage(page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 4;

        assertThat(metersDataRepository.getPage(page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(DatabaseInterectionException.class, ()->metersDataRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(DatabaseInterectionException.class, ()->metersDataRepository.getPage(page, count));
    }
}