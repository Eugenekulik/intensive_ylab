package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.TestConfig;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.repository.JdbcMetersTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class JdbcMetersTypeRepositoryTest {


    @Autowired
    private MetersTypeRepository metersTypeRepository;



    @Test
    void testSave_shouldReturnSavedMetersType(){
        MetersType metersType = MetersType.builder()
            .name("electricity")
            .build();

        assertThat(metersTypeRepository.save(metersType))
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("name", "electricity");
    }


    @Test
    void testFindById_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        long id = 1L;

        assertThat(metersTypeRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        long id = 1000L;

        assertThat(metersTypeRepository.findById(id))
            .isEmpty();
    }

    @Test
    void testFindByName_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        String name = "warm_water";

        assertThat(metersTypeRepository.findByName(name))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    void testFindByName_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        String name = "gas";

        assertThat(metersTypeRepository.findByName(name))
            .isEmpty();
    }

    @Test
    void testFindAll_shouldReturnListOfAllMetersData(){

        assertThat(metersTypeRepository.findAll())
            .anyMatch(metersType -> metersType.getName().equals("warm_water"))
            .anyMatch(metersType -> metersType.getName().equals("cold_water"))
            .anyMatch(metersType -> metersType.getName().equals("heating"))
            .size().isGreaterThanOrEqualTo(3);
    }

}