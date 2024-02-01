package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.utils.IncrementSequence;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryAgreementRepositoryTest {

    private static AgreementRepository agreementRepository;

    @BeforeAll
    static void setUp(){
        agreementRepository = new InMemoryAgreementRepository(new IncrementSequence(1L, 1L));
    }


    @Test
    @Order(1)
    void testSave_shouldReturnSavedAgreement(){
        Agreement agreement = Agreement
            .builder()
            .addressId(1L)
            .userId(1L)
            .build();

        assertThat(agreementRepository.save(agreement))
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("addressId", 1L)
            .hasFieldOrPropertyWithValue("userId", 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfAgreement_whenAgreementExists(){
        long id = 1L;

        assertThat(agreementRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("userId", 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenAgreementNotExists(){
        long id = 1000L;

        assertThat(agreementRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByUserIdAndAddressId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 1L;
        long addressId = 1L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    void testFindByUserIdAndAddressId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 2L;
        long addressId = 2L;

        assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByUserId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
        long userId = 1L;

        assertThat(agreementRepository.findByUserId(userId))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    void testFindByUserId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 2L;

        assertThat(agreementRepository.findByUserId(userId))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfAgreement_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        assertThat(agreementRepository.getPage(page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("addressId", 1L);
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 1;
        int count = 1;

        assertThat(agreementRepository.getPage(page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(IllegalArgumentException.class, ()->agreementRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(IllegalArgumentException.class, ()->agreementRepository.getPage(page, count));
    }


}