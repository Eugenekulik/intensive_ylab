package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.utils.IncrementSequence;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryMetersTypeRepositoryTest {


    private static MetersTypeRepository metersTypeRepository;

    @BeforeAll
    static void setUp() {
        metersTypeRepository = new InMemoryMetersTypeRepository(new IncrementSequence(1L, 1L));
    }



    @Test
    @Order(1)
    void testSave_shouldReturnSavedMetersType(){
        MetersType metersType = MetersType.builder()
            .name("warm_water")
            .build();

        assertThat(metersTypeRepository.save(metersType))
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }


    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        long id = 1L;

        assertThat(metersTypeRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        long id = 2L;

        assertThat(metersTypeRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByName_shouldReturnOptionalOfMetersType_whenMetersTypeExists(){
        String name = "warm_water";

        assertThat(metersTypeRepository.findByName(name))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }

    @Test
    @Order(2)
    void testFindByName_shouldReturnEmptyOptional_whenMetersTypeNotExists(){
        String name = "cold_water";

        assertThat(metersTypeRepository.findByName(name))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindAll_shouldReturnListOfAllMetersData(){

        assertThat(metersTypeRepository.findAll())
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "warm_water");
    }


}