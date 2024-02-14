package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.service.impl.MetersDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MetersDataServiceTest extends TestConfigurationEnvironment {


    private MetersDataService metersDataService;
    private MetersDataRepository metersDataRepository;

    @BeforeEach
    void setUp() {
        metersDataRepository = mock(MetersDataRepository.class);
        metersDataService = new MetersDataServiceImpl(metersDataRepository);
    }

    @Test
    void testCreate_shouldSaveMetersData_whenConditionsAreMet() {
        MetersData metersData = mock(MetersData.class);

        when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
        when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
            .thenReturn(Optional.empty());
        when(metersDataRepository.save(metersData))
            .thenReturn(metersData);
        assertEquals(metersData, metersDataService.create(metersData));

        verify(metersDataRepository).findByAgreementAndTypeAndMonth(
            metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()
        );
        verify(metersDataRepository).save(metersData);
    }

    @Test
    void testCreate_shouldThrowException_whenReadingsAlreadySubmittedThisMonth() {
        MetersData metersData = mock(MetersData.class);

        when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
        when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
            .thenReturn(Optional.of(new MetersData()));

        assertThrows(IllegalArgumentException.class, () -> metersDataService.create(metersData));

        verify(metersDataRepository).findByAgreementAndTypeAndMonth(
            metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()
        );
        verify(metersDataRepository, never()).save(any());
    }

    @Test
    void testFindByAgreementAndTypeAndMonth_shouldReturnMetersData_whenExists() {
        Long agreementId = 1L;
        Long metersTypeId = 2L;
        LocalDate localDate = LocalDate.now();
        MetersData metersData = mock(MetersData.class);

        when(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate))
            .thenReturn(Optional.of(metersData));

        assertEquals(metersData, metersDataService.findByAgreementAndTypeAndMonth(agreementId,
            metersTypeId, localDate));

        verify(metersDataRepository).findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate);
    }

    @Test
    void testFindByAgreementAndTypeAndMonth_shouldThrowException_whenNotExists() {
        Long agreementId = 1L;
        Long metersTypeId = 2L;
        LocalDate localDate = LocalDate.now();

        when(metersDataRepository.findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> metersDataService
            .findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate));

        verify(metersDataRepository)
            .findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate);
    }


    @Test
    void testFindLastByAgreementAndType_shouldReturnMetersData_whenExists() {
        Long agreementId = 1L;
        Long metersTypeId = 2L;
        MetersData metersData = mock(MetersData.class);

        when(metersDataRepository.findLastByAgreementAndType(agreementId, metersTypeId))
            .thenReturn(Optional.of(metersData));

        assertEquals(metersData, metersDataService
            .findLastByAgreementAndType(agreementId,metersTypeId));

        verify(metersDataRepository).findLastByAgreementAndType(agreementId, metersTypeId);
    }

    @Test
    void testFindLastByAgreementAndType_shouldThrowException_whenNotExists() {
        Long agreementId = 1L;
        Long metersTypeId = 2L;

        when(metersDataRepository.findLastByAgreementAndType(agreementId, metersTypeId))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> metersDataService
            .findLastByAgreementAndType(agreementId, metersTypeId));

        verify(metersDataRepository).findLastByAgreementAndType(agreementId, metersTypeId);
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        List<MetersData> metersData = mock(List.class);

        when(metersDataRepository.getPage(pageable)).thenReturn(metersData);

        assertEquals(metersData, metersDataService.getPage(pageable));

        verify(metersDataRepository).getPage(pageable);
    }

    @Test
    void testGetPage_shouldThrowException_whenNotValidPageable() {
        Pageable pageable = mock(Pageable.class);

        when(metersDataRepository.getPage(pageable))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> metersDataService
            .getPage(pageable));

        verify(metersDataRepository).getPage(pageable);
    }
}