package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.out.dao.jdbc.repository.JdbcAgreementRepository;
import by.eugenekulik.out.dao.jdbc.utils.ConnectionPool;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class JdbcAgreementRepositoryTest extends ConfiguraionEnviroment{



    private static JdbcAgreementRepository agreementRepository;

    @BeforeAll
    static void setUp(){
        ConnectionPool connectionPool =
            new ConnectionPool(postgreSQLContainer.getDataSource(), 1, 30);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionPool);
        agreementRepository = new JdbcAgreementRepository(jdbcTemplate);
    }



    @Test
    void testSave_shouldReturnSavedAgreement(){
        Agreement agreement = Agreement
            .builder()
            .addressId(1L)
            .userId(1L)
            .build();

        assertThat(agreementRepository.save(agreement))
            .isNotNull()
            .hasFieldOrPropertyWithValue("addressId", 1L)
            .hasFieldOrPropertyWithValue("userId", 1L);
    }

    @Test
    void testFindById_shouldReturnOptionalOfAgreement_whenAgreementExists(){
        long id = 1L;

        assertThat(agreementRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("userId", 2L);
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenAgreementNotExists(){
        long id = 1000L;

        assertThat(agreementRepository.findById(id))
            .isEmpty();
    }

    @Test
    void testFindByUserIdAndAddressId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 2L;
        long addressId = 1L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testFindByUserIdAndAddressId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 3L;
        long addressId = 3L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .isEmpty();
    }

    @Test
    void testFindByUserId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 2L;
        Pageable pageable = new Pageable(0,1);

        assertThat(agreementRepository.findByUserId(userId, pageable))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testFindByUserId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 3L;
        Pageable pageable = new Pageable(0,1);

        assertThat(agreementRepository.findByUserId(userId, pageable))
            .isEmpty();
    }


    @Test
    void testGetPage_shouldReturnListOfAgreement_whenPageAndCountValid(){
        Pageable pageable = new Pageable(0,1);

        assertThat(agreementRepository.getPage(pageable))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("addressId", 1L);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = new Pageable(4,4);
        assertThat(agreementRepository.getPage(pageable))
            .isEmpty();
    }

    @Test
    void testGetPage_shouldThrowException_whenPageNegative(){
        Pageable pageable = new Pageable(-1, 1);

        assertThrows(DatabaseInterectionException.class, ()->agreementRepository.getPage(pageable));
    }

    @Test
    void testGetPage_shouldThrowException_whenCountNegative(){
        Pageable pageable = new Pageable(0,-1);

        assertThrows(DatabaseInterectionException.class, ()->agreementRepository.getPage(pageable));
    }

}
