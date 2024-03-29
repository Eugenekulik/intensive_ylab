package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testFindById_shouldReturnOptionalOfUser_whenUserExists(){

            assertThat(userRepository.findById(1L))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "admin");
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
        void testFindByEmail_shouldReturnOptionalOfUser_whenUserExists(){
            String email = "user@mail.ru";

            assertThat(userRepository.findByEmail(email))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user1")
                .hasFieldOrPropertyWithValue("email", "user@mail.ru");
        }

        @Test
        void testGetPage_shouldReturnListOfUser_whenPageAndCountValid(){
            Pageable pageable = PageRequest.of(0,2);

            assertThat(userRepository.getPage(pageable))
                .hasSize(2);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testFindById_shouldReturnEmptyOptional_whenUserNotExists(){
            assertThat(userRepository.findById(1000L))
                .isEmpty();
        }

        @Test
        void testFindByUsername_shouldReturnOptionalOfUser_whenUserExists(){
            String username = "user1";

            assertThat(userRepository.findByUsername(username))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user1");
        }

        @Test
        void testFindByUsername_shouldReturnEmptyOptional_whenUserNotExists(){
            String username = "any";
            assertThat(userRepository.findByUsername(username))
                .isEmpty();
        }

        @Test
        void testFindByEmail_shouldReturnEmptyOptional_whenUserNotExists(){
            String email = "any@mail.ru";

            assertThat(userRepository.findByEmail(email))
                .isEmpty();
        }

        @Test
        void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
            Pageable pageable = PageRequest.of(4,4);

            assertThat(userRepository.getPage(pageable))
                .isEmpty();
        }
    }
}