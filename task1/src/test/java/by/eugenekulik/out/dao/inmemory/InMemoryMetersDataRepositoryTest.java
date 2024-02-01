package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.utils.IncrementSequence;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryMetersDataRepositoryTest {

    private static MetersDataRepository metersDataRepository;

    @BeforeAll
    static void setUp() {
        metersDataRepository = new InMemoryMetersDataRepository(new IncrementSequence(1L, 1L));
    }

    @Test
    @Order(1)
    void testSave_shouldReturnSavedMetersData(){
        MetersData metersData = MetersData.builder()
            .agreementId(1L)
            .placedAt(LocalDateTime.now())
            .value(100.0)
            .metersTypeId(1L)
            .build();

        assertThat(metersDataRepository.save(metersData))
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfMetersData_whenIdIsValid(){
        long id = 1L;

        assertThat(metersDataRepository.findById(id))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0)
            .hasFieldOrPropertyWithValue("metersTypeId", 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnEmptyOptional_whenIdIsNotValid(){
        long id = 1000L;

        assertThat(metersDataRepository.findById(id))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindByAgreementAndTypeAndMonth_shouldReturnOptionalOfMetersData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;
        LocalDate localDate = LocalDate.now();

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId , localDate))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testFindByAgreementAndTypeAndMonth_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;
        LocalDate localDate = LocalDate.now();

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId, localDate))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testFindLastByAgreementAndType_shouldReturnOptionalOfMeterData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
    @Order(2)
    void testFindLastByAgreementAndType_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnListOfMetersData_whenArgumentAreValid(){
        int page = 0;
        int count = 1;
        long agreementId = 1L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnEmptyList_whenAgreementNotValid(){
        int page = 0;
        int count = 1;
        long agreementId = 2L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPageByAgreement_shouldReturnEmptyList_whenPageNotValid(){
        int page = 1;
        int count = 1;
        long agreementId = 1L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }


    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfMetersData_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        assertThat(metersDataRepository.getPage(page, count))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 1;
        int count = 1;

        assertThat(metersDataRepository.getPage(page, count))
            .isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(IllegalArgumentException.class, ()->metersDataRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(IllegalArgumentException.class, ()->metersDataRepository.getPage(page, count));
    }
}