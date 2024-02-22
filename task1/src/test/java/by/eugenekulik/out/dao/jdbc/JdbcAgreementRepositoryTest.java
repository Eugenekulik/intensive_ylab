package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.TestConfig;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
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
class JdbcAgreementRepositoryTest {


    @Autowired
    private AgreementRepository agreementRepository;


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
        Pageable pageable = PageRequest.of(0,1);

        assertThat(agreementRepository.findByUserId(userId, pageable))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testFindByUserId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
        long userId = 3L;
        Pageable pageable = PageRequest.of(0,1);

        assertThat(agreementRepository.findByUserId(userId, pageable))
            .isEmpty();
    }


    @Test
    void testGetPage_shouldReturnListOfAgreement_whenPageAndCountValid(){
        Pageable pageable = PageRequest.of(0,1);

        assertThat(agreementRepository.getPage(pageable))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("addressId", 1L);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = PageRequest.of(4,4);
        assertThat(agreementRepository.getPage(pageable))
            .isEmpty();
    }


}
