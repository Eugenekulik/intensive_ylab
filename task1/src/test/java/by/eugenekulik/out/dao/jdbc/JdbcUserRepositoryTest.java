package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.jdbc.repository.JdbcUserRepository;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcUserRepositoryTest {

    @Container
    private static  PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer("postgres:15-alpine")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    private static JdbcUserRepository userRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer.start();
        DataSource dataSource = new DataSource("jdbc:postgresql://localhost:"+
                                    postgreSQLContainer.getFirstMappedPort() + "/test","test",
            "test", "org.postgresql.Driver");
        ConnectionPool connectionPool = ConnectionPool
            .createConnectionPool(dataSource, 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        userRepository = new JdbcUserRepository(jdbcTemplate);

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

    @AfterAll
    static void destroy(){
        postgreSQLContainer.stop();
    }


    @Test
    void testFindById_shouldReturnOptionalOfUser_whenUserExists(){

        assertThat(userRepository.findById(1L))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("username", "admin");
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenUserNotExists(){
        assertThat(userRepository.findById(1000L))
            .isEmpty();
    }



    @Test
    @Order(1)
    void testSave_shouldReturnSavedUser(){
        User user = User.builder()
            .username("saved")
            .password("password")
            .email("saved@mail.ru")
            .role(Role.CLIENT)
            .build();

        assertThat(userRepository.save(user))
            .isNotNull()
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("username", "saved");
    }


    @Test
    @Order(2)
    void testFindByUsername_shouldReturnOptionalOfUser_whenUserExists(){
        String username = "user";

        assertThat(userRepository.findByUsername(username))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testFindByUsername_shouldReturnEmptyOptional_whenUserNotExists(){
        String username = "any";
        assertThat(userRepository.findByUsername(username))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testFindByEmail_shouldReturnOptionalOfUser_whenUserExists(){
        String email = "user@mail.ru";

        assertThat(userRepository.findByEmail(email))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("username", "user")
            .hasFieldOrPropertyWithValue("email", "user@mail.ru");
    }

    @Test
    @Order(2)
    void testFindByEmail_shouldReturnEmptyOptional_whenUserNotExists(){
        String email = "any@mail.ru";

        assertThat(userRepository.findByEmail(email))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfUser_whenPageAndCountValid(){
        int page = 0;
        int count = 2;

        assertThat(userRepository.getPage(page, count))
            .hasSize(2);
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 2;

        assertThat(userRepository.getPage(page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(IllegalArgumentException.class, ()->userRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(IllegalArgumentException.class, ()->userRepository.getPage(page, count));
    }


}