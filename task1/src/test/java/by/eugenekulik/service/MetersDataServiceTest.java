package by.eugenekulik.service;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MetersDataServiceTest {


    private MetersDataService metersDataService;
    private MetersDataRepository metersDataRepository;

    @BeforeEach
    void setUp() {
        metersDataRepository = mock(MetersDataRepository.class);
        metersDataService = new MetersDataService(metersDataRepository);
    }

    @Test
    void testCreate_shouldSaveMetersData_whenConditionsAreMet() {
        MetersData metersData = mock(MetersData.class);

        when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
        when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
            .thenReturn(Optional.empty());
        when(metersDataRepository.save(metersData)).thenReturn(metersData);

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
        int page = 1;
        int count = 10;
        List<MetersData> metersData = mock(List.class);

        when(metersDataRepository.getPage(page, count)).thenReturn(metersData);

        assertEquals(metersData, metersDataService.getPage(page, count));

        verify(metersDataRepository).getPage(page, count);
    }

    @Test
    void testGetPage_shouldThrowException_whenCountIsNotPositive() {
        int count = 0;
        int page = 1;

        assertThrows(IllegalArgumentException.class, () -> metersDataService
            .getPage(page, count), "count must be positive");

        verify(metersDataRepository, never()).getPage(anyInt(), anyInt());
    }

    @Test
    void testGetPage_shouldThrowException_whenPageIsNegative() {
        int page = -1;
        int count = 10;

        assertThrows(IllegalArgumentException.class, () -> metersDataService.getPage(page, count),
            "page must not be negative");

        verify(metersDataRepository, never()).getPage(anyInt(), anyInt());
    }
}