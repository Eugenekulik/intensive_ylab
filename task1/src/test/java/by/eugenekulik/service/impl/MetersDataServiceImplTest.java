package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@UnitTest
@WebAppConfiguration
class MetersDataServiceImplTest {

    @InjectMocks
    private MetersDataServiceImpl metersDataService;
    @Mock
    private MetersDataRepository metersDataRepository;
    @Mock
    private MetersTypeRepository metersTypeRepository;
    @Mock
    private MetersDataMapper mapper;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testCreate_shouldSaveMetersData_whenConditionsAreMet() {
            MetersData metersData = mock(MetersData.class);
            MetersDataRequestDto metersDataRequestDto = mock(MetersDataRequestDto.class);
            MetersDataResponseDto metersDataResponseDto = mock(MetersDataResponseDto.class);

            when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
            when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
                .thenReturn(Optional.empty());
            when(metersDataRepository.save(metersData))
                .thenReturn(metersData);
            when(mapper.fromMetersDataDto(metersDataRequestDto)).thenReturn(metersData);
            when(mapper.fromMetersData(metersData)).thenReturn(metersDataResponseDto);

            assertEquals(metersDataResponseDto, metersDataService.create(metersDataRequestDto));

            verify(metersDataRepository).findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any());
            verify(metersDataRepository).save(metersData);
        }

        @Test
        void testFindLastByAgreementAndType_shouldReturnMetersData_whenExists() {
            Long agreementId = 1L;
            String type = "type";
            MetersType metersType = MetersType.builder().id(1L).build();
            MetersDataResponseDto metersDataResponseDto = mock(MetersDataResponseDto.class);
            MetersData metersData = mock(MetersData.class);

            when(mapper.fromMetersData(metersData)).thenReturn(metersDataResponseDto);
            when(metersTypeRepository.findByName(type)).thenReturn(Optional.of(metersType));
            when(metersDataRepository.findLastByAgreementAndType(agreementId, 1L))
                .thenReturn(Optional.of(metersData));

            assertEquals(metersDataResponseDto, metersDataService
                .findLastByAgreementAndType(agreementId,type));

            verify(metersDataRepository).findLastByAgreementAndType(agreementId, 1L);
            verify(metersTypeRepository).findByName(type);
        }

        @Test
        void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
            Pageable pageable = PageRequest.ofSize(1);
            MetersData metersData = mock(MetersData.class);
            MetersDataResponseDto metersDataResponseDto = mock(MetersDataResponseDto.class);
            List<MetersData> metersDataList = List.of(metersData);

            when(mapper.fromMetersData(metersData)).thenReturn(metersDataResponseDto);
            when(metersDataRepository.getPage(pageable)).thenReturn(metersDataList);

            assertThat(metersDataService.getPage(pageable))
                .hasSize(1)
                .first()
                .isEqualTo(metersDataResponseDto);

            verify(metersDataRepository).getPage(pageable);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testCreate_shouldThrowException_whenReadingsAlreadySubmittedThisMonth() {
            MetersData metersData = mock(MetersData.class);
            MetersDataRequestDto metersDataRequestDto = mock(MetersDataRequestDto.class);

            when(mapper.fromMetersDataDto(metersDataRequestDto)).thenReturn(metersData);
            when(metersData.getPlacedAt()).thenReturn(LocalDateTime.now());
            when(metersDataRepository.findByAgreementAndTypeAndMonth(anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(new MetersData()));

            assertThrows(IllegalArgumentException.class, () -> metersDataService.create(metersDataRequestDto));

            verify(metersDataRepository).findByAgreementAndTypeAndMonth(
                metersData.getAgreementId(),
                metersData.getMetersTypeId(),
                metersData.getPlacedAt().toLocalDate()
            );
            verify(metersDataRepository, never()).save(any());
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
    }
}