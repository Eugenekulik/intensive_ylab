package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.TestConfig;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class JdbcUserRepositoryTest {


    @Autowired
    private UserRepository userRepository;



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
        Pageable pageable = PageRequest.of(0,2);

        assertThat(userRepository.getPage(pageable))
            .hasSize(2);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = PageRequest.of(4,4);

        assertThat(userRepository.getPage(pageable))
            .isEmpty();
    }



}