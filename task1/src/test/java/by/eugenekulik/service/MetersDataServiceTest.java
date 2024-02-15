package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.service.impl.MetersDataServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MetersDataServiceTest extends TestConfigurationEnvironment {


    private MetersDataService metersDataService;
    private MetersDataRepository metersDataRepository;
    private MetersTypeRepository metersTypeRepository;
    private MetersDataMapper mapper;

    @BeforeEach
    void setUp() {
        metersDataRepository = mock(MetersDataRepository.class);
        metersTypeRepository = mock(MetersTypeRepository.class);
        mapper = mock(MetersDataMapper.class);
        metersDataService = new MetersDataServiceImpl(metersDataRepository, metersTypeRepository, mapper);
    }

    @Test
    void testCreate_shouldSaveMetersData_whenConditionsAreMet() {
        MetersData metersData = mock(MetersData.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);

        when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
        when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
            .thenReturn(Optional.empty());
        when(metersDataRepository.save(metersData))
            .thenReturn(metersData);
        when(mapper.fromMetersDataDto(metersDataDto)).thenReturn(metersData);
        when(mapper.fromMetersData(metersData)).thenReturn(metersDataDto);

        assertEquals(metersDataDto, metersDataService.create(metersDataDto));

        verify(metersDataRepository).findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any());
        verify(metersDataRepository).save(metersData);
    }

    @Test
    void testCreate_shouldThrowException_whenReadingsAlreadySubmittedThisMonth() {
        MetersData metersData = mock(MetersData.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);

        when(mapper.fromMetersDataDto(metersDataDto)).thenReturn(metersData);
        when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
        when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
            .thenReturn(Optional.of(new MetersData()));

        assertThrows(IllegalArgumentException.class, () -> metersDataService.create(metersDataDto));

        verify(metersDataRepository).findByAgreementAndTypeAndMonth(
            metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()
        );
        verify(metersDataRepository, never()).save(any());
    }






    @Test
    void testFindLastByAgreementAndType_shouldReturnMetersData_whenExists() {
        Long agreementId = 1L;
        String type = "type";
        MetersType metersType = MetersType.builder().id(1L).build();
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        MetersData metersData = mock(MetersData.class);

        when(mapper.fromMetersData(metersData)).thenReturn(metersDataDto);
        when(metersTypeRepository.findByName(type)).thenReturn(Optional.of(metersType));
        when(metersDataRepository.findLastByAgreementAndType(agreementId, 1L))
            .thenReturn(Optional.of(metersData));

        assertEquals(metersDataDto, metersDataService
            .findLastByAgreementAndType(agreementId,type));

        verify(metersDataRepository).findLastByAgreementAndType(agreementId, 1L);
        verify(metersTypeRepository).findByName(type);
    }

    @Test
    void testFindLastByAgreementAndType_shouldThrowException_whenNotExists() {
        Long agreementId = 1L;
        MetersType metersType = MetersType.builder().id(1L).name("type").build();

        when(metersDataRepository.findLastByAgreementAndType(agreementId, metersType.getId()))
            .thenReturn(Optional.empty());
        when(metersTypeRepository.findByName(metersType.getName())).thenReturn(Optional.of(metersType));
        assertThrows(IllegalArgumentException.class,()-> metersDataService
                .findLastByAgreementAndType(agreementId, metersType.getName()),
            "not found metersData");

        verify(metersDataRepository).findLastByAgreementAndType(agreementId, metersType.getId());
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        MetersData metersData = mock(MetersData.class);
        MetersDataDto metersDataDto = mock(MetersDataDto.class);
        List<MetersData> metersDataList = List.of(metersData);

        when(mapper.fromMetersData(metersData)).thenReturn(metersDataDto);
        when(metersDataRepository.getPage(pageable)).thenReturn(metersDataList);

        assertThat(metersDataService.getPage(pageable))
            .hasSize(1)
            .first()
            .isEqualTo(metersDataDto);

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