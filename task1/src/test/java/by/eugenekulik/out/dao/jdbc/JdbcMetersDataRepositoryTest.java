package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJdbcTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcMetersDataRepositoryTest{


    @Autowired
    private MetersDataRepository metersDataRepository;




    @Test
    void testSave_shouldReturnSavedMetersData(){
        MetersData metersData = MetersData.builder()
            .agreementId(1L)
            .placedAt(LocalDateTime.now())
            .value(100.0)
            .metersTypeId(1L)
            .build();

        assertThat(metersDataRepository.save(metersData))
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
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
    void testFindById_shouldReturnEmptyOptional_whenIdIsNotValid(){
        long id = 1000L;

        assertThat(metersDataRepository.findById(id))
            .isEmpty();
    }

    @Test
    void testFindByAgreementAndTypeAndMonth_shouldReturnOptionalOfMetersData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;
        LocalDate localDate = LocalDate.of(2024,2,4);

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId , localDate))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    void testFindByAgreementAndTypeAndMonth_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;
        LocalDate localDate = LocalDate.now();

        assertThat(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId,
            typeId, localDate))
            .isEmpty();
    }

    @Test
    void testFindLastByAgreementAndType_shouldReturnOptionalOfMeterData_whenMetersDataExists(){
        long agreementId = 1L;
        long typeId = 1L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isPresent()
            .get()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }


    @Test
    void testFindLastByAgreementAndType_shouldReturnEmptyOptional_whenMetersDataNotExists(){
        long agreementId = 1L;
        long typeId = 2L;

        assertThat(metersDataRepository.findLastByAgreementAndType(agreementId, typeId))
            .isEmpty();
    }


    @Test
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
    void testGetPageByAgreement_shouldReturnEmptyList_whenAgreementNotValid(){
        int page = 0;
        int count = 1;
        long agreementId = 2L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }

    @Test
    void testGetPageByAgreement_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 4;
        int count = 4;
        long agreementId = 1L;

        assertThat(metersDataRepository.getPageByAgreement(agreementId, page, count))
            .isEmpty();
    }


    @Test
    void testGetPage_shouldReturnListOfMetersData_whenPageAndCountValid(){
        Pageable pageable = PageRequest.of(0,1);

        assertThat(metersDataRepository.getPage(pageable))
            .hasSize(1)
            .first()
            .hasFieldOrPropertyWithValue("value", 100.0);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        Pageable pageable = PageRequest.of(4,4);

        assertThat(metersDataRepository.getPage(pageable))
            .isEmpty();
    }

}