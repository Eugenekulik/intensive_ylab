package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.PostgresTestContainer;
import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.out.dao.jdbc.repository.JdbcUserRepository;
import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;



class JdbcUserRepositoryTest extends TestConfigurationEnvironment {


    private static JdbcUserRepository userRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer = PostgresTestContainer.getInstance();
        ConnectionPool connectionPool =
            new ConnectionPool(postgreSQLContainer.getDataSource(), 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        userRepository = new JdbcUserRepository(jdbcTemplate);
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
    void testFindByUsername_shouldReturnOptionalOfUser_whenUserExists(){
        String username = "user";

        assertThat(userRepository.findByUsername(username))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    void testFindByUsername_shouldReturnEmptyOptional_whenUserNotExists(){
        String username = "any";
        assertThat(userRepository.findByUsername(username))
            .isEmpty();
    }


    @Test
    void testFindByEmail_shouldReturnOptionalOfUser_whenUserExists(){
        String email = "user@mail.ru";

        assertThat(userRepository.findByEmail(email))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("username", "user")
            .hasFieldOrPropertyWithValue("email", "user@mail.ru");
    }

    @Test
    void testFindByEmail_shouldReturnEmptyOptional_whenUserNotExists(){
        String email = "any@mail.ru";

        assertThat(userRepository.findByEmail(email))
            .isEmpty();
    }

    @Test
    void testGetPage_shouldReturnListOfUser_whenPageAndCountValid(){
        Pageable pageable = new Pageable(0,2);

        assertThat(userRepository.getPage(pageable))
            .hasSize(2);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = new Pageable(4,2);

        assertThat(userRepository.getPage(pageable))
            .isEmpty();
    }

    @Test
    void testGetPage_shouldThrowException_whenPageNegative(){
        Pageable pageable = new Pageable(-1,1);

        assertThrows(DatabaseInterectionException.class, ()->userRepository.getPage(pageable));
    }

    @Test
    void testGetPage_shouldThrowException_whenCountNegative(){
        Pageable pageable = new Pageable(0,-1);

        assertThrows(DatabaseInterectionException.class, ()->userRepository.getPage(pageable));
    }


}