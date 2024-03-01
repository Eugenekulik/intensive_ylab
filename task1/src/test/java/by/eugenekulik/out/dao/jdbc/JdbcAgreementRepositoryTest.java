package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
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
class JdbcAgreementRepositoryTest {
    @Autowired
    private AgreementRepository agreementRepository;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testSave_shouldReturnSavedAgreement(){
            Agreement agreement = Agreement
                .builder()
                .addressId(2L)
                .userId(3L)
                .build();

            assertThat(agreementRepository.save(agreement))
                .isNotNull()
                .hasFieldOrPropertyWithValue("addressId", 2L)
                .hasFieldOrPropertyWithValue("userId", 3L);
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
        void testFindByUserIdAndAddressId_shouldReturnListOfAgreement_whenExistsAtLeastOneAgreement(){
            long userId = 2L;
            long addressId = 1L;

            assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", 1L);
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
        void testGetPage_shouldReturnListOfAgreement_whenPageAndCountValid(){
            Pageable pageable = PageRequest.of(0,1);

            assertThat(agreementRepository.getPage(pageable))
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("addressId", 1L);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testFindById_shouldReturnEmptyOptional_whenAgreementNotExists(){
            long id = 1000L;

            assertThat(agreementRepository.findById(id))
                .isEmpty();
        }

        @Test
        void testFindByUserIdAndAddressId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
            long userId = 3L;
            long addressId = 3L;

            assertThat(agreementRepository.findByUserIdAndAddressId(userId, addressId))
                .isEmpty();
        }

        @Test
        void testFindByUserId_shouldReturnEmptyList_whenNotExistsAtLeastOneAgreement(){
            long userId = 3L;
            Pageable pageable = PageRequest.of(0,1);

            assertThat(agreementRepository.findByUserId(userId, pageable))
                .isEmpty();
        }

        @Test
        void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
            Pageable pageable = PageRequest.of(4,4);
            assertThat(agreementRepository.getPage(pageable))
                .isEmpty();
        }
    }
}
