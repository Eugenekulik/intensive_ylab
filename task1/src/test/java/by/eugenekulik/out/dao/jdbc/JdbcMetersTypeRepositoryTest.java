package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.PostgresTestContainer;
import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.jdbc.repository.JdbcMetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



class JdbcMetersTypeRepositoryTest extends TestConfigurationEnvironment {



    private static JdbcMetersTypeRepository metersTypeRepository;

    @BeforeAll
    static void setUp(){
        postgreSQLContainer = PostgresTestContainer.getInstance();
        ConnectionPool connectionPool =
            new ConnectionPool(postgreSQLContainer.getDataSource(), 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        metersTypeRepository = new JdbcMetersTypeRepository(jdbcTemplate);
    }
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