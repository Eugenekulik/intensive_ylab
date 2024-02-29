package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcMetersTypeRepositoryTest {
    @Autowired
    private MetersTypeRepository metersTypeRepository;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
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
        void testFindByName_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
            String name = "warm_water";

            assertThat(metersTypeRepository.findByName(name))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "warm_water");
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

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testFindById_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
            long id = 1000L;

            assertThat(metersTypeRepository.findById(id))
                .isEmpty();
        }

        @Test
        void testFindByName_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
            String name = "gas";

            assertThat(metersTypeRepository.findByName(name))
                .isEmpty();
        }
    }
}