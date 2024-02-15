package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.PostgresTestContainer;
import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.jdbc.repository.JdbcAddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class JdbcAddressRepositoryTest extends TestConfigurationEnvironment {


    private static JdbcAddressRepository addressRepository;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        ConnectionPool connectionPool =
            new ConnectionPool(postgreSQLContainer.getDataSource(), 2, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        addressRepository = new JdbcAddressRepository(jdbcTemplate);
    }

    @Test
    void testSave_shouldSaveAndReturnAddress(){
        Address address = Address.builder()
            .region("Grodno region")
            .district("Volkovysk district")
            .city("Volkovysk")
            .street("Gorbatovo")
            .house("9")
            .apartment("2")
            .build();

        assertThat(addressRepository.save(address))
            .isNotNull()
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("region", "Grodno region");
    }

    @Test
    void testFindById_shouldReturnOptionalOfAddress_whenAddressExists(){
        assertTrue(addressRepository.findById(1L).isPresent());
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenAddressNotExists(){
        assertTrue(addressRepository.findById(1000L).isEmpty());
    }




    @Test
    void testGetPage_shouldReturnListOfAddresses_whenPageAndCountValid(){
        Pageable pageable = new Pageable(0,2);
        List<Address> result = addressRepository.getPage(pageable);

        assertThat(result)
            .anyMatch(address -> address.getRegion().equals("Minsk region"));
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = new Pageable(4,4);

        List<Address> result = addressRepository.getPage(pageable);

        assertThat(result).isEmpty();
    }

    @Test
    void testGetPage_shouldThrowException_whenPageNegative(){
        Pageable pageable = new Pageable(-1,1);

        assertThrows(DatabaseInterectionException.class, ()->addressRepository.getPage(pageable));
    }

    @Test
    void testGetPage_shouldThrowException_whenCountNegative(){
        Pageable pageable = new Pageable(0, -1);

        assertThrows(DatabaseInterectionException.class, ()->addressRepository.getPage(pageable));
    }

}