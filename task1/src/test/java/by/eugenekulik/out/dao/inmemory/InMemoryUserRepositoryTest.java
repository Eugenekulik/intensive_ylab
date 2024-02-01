package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.utils.IncrementSequence;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryUserRepositoryTest {


    private static UserRepository userRepository;

    @BeforeAll
    public static void setUp(){
        userRepository = new InMemoryUserRepository(new IncrementSequence(1L, 1L));
    }


    @Test
    @Order(1)
    void testSave_shouldReturnSavedUser(){
        User user = User.builder()
            .username("user")
            .password("user")
            .email("user@mail.ru")
            .role(Role.CLIENT)
            .build();

        assertThat(userRepository.save(user))
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfUser_whenUserExists(){
        long id = 1L;

        assertThat(userRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenUserNotExists(){
        long id = 2L;

        assertThat(userRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByUsername_shouldReturnOptionalOfUser_whenUserExists(){
        String username = "user";

        assertThat(userRepository.findByUsername(username))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testFindByUsername_shouldReturnEmptyOptional_whenUserNotExists(){
        String username = "admin";

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
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testFindByEmail_shouldReturnEmptyOptional_whenUserNotExists(){
        String email = "admin@mail.ru";

        assertThat(userRepository.findByEmail(email))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfUser_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        assertThat(userRepository.getPage(page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("username", "user");
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 1;
        int count = 1;

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