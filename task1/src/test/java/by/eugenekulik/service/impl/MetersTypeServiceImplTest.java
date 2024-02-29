package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.MetersTypeMapper;
import by.eugenekulik.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@UnitTest
@WebAppConfiguration
class MetersTypeServiceImplTest {

    @InjectMocks
    private MetersTypeServiceImpl metersTypeService;
    @Mock
    private MetersTypeRepository metersTypeRepository;
    @Mock
    private MetersTypeMapper mapper;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testCreate_shouldSaveMetersType_whenNotExists() {
            MetersType metersType = mock(MetersType.class);
            MetersTypeRequestDto metersTypeRequestDto = mock(MetersTypeRequestDto.class);
            MetersTypeResponseDto metersTypeResponseDto = mock(MetersTypeResponseDto.class);

            when(metersTypeRepository.save(metersType)).thenReturn(metersType);
            when(mapper.fromMetersTypeDto(metersTypeRequestDto)).thenReturn(metersType);
            when(mapper.fromMetersType(metersType)).thenReturn(metersTypeResponseDto);

            assertEquals(metersTypeResponseDto, metersTypeService.create(metersTypeRequestDto));

            verify(metersTypeRepository).save(metersType);
        }

        @Test
        void testFindByName_shouldReturnMetersType_whenExists() {
            String metersTypeName = "warm_water";
            MetersType metersType = mock(MetersType.class);
            MetersTypeResponseDto metersTypeResponseDto = mock(MetersTypeResponseDto.class);

            when(mapper.fromMetersType(metersType)).thenReturn(metersTypeResponseDto);
            when(metersTypeRepository.findByName(metersTypeName)).thenReturn(Optional.of(metersType));

            assertEquals(metersTypeResponseDto, metersTypeService.findByName(metersTypeName));

            verify(metersTypeRepository).findByName(metersTypeName);
        }

        @Test
        void testFindAll_shouldReturnAllMetersTypes() {
            MetersType metersType = mock(MetersType.class);
            MetersTypeResponseDto metersDataResponseDto = mock(MetersTypeResponseDto.class);
            List<MetersType> metersTypes = List.of(metersType);

            when(mapper.fromMetersType(metersType)).thenReturn(metersDataResponseDto);
            when(metersTypeRepository.findAll()).thenReturn(metersTypes);

            assertThat(metersTypeService.findAll())
                .hasSize(1)
                .first()
                .isEqualTo(metersDataResponseDto);

            verify(metersTypeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testCreate_shouldThrowException_whenExists() {
            MetersType metersType = mock(MetersType.class);
            MetersTypeRequestDto metersTypeRequestDto = mock(MetersTypeRequestDto.class);

            when(mapper.fromMetersTypeDto(metersTypeRequestDto)).thenReturn(metersType);
            when(metersTypeRepository.save(metersType))
                .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> metersTypeService.create(metersTypeRequestDto));

            verify(metersTypeRepository).save(metersType);
        }

        @Test
        void testFindByName_shouldThrowException_whenNotExists() {
            String name = "electric";

            when(metersTypeRepository.findByName(name)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class,
                () -> metersTypeService.findByName(name),
                "not found metersType with name: " + name);

            verify(metersTypeRepository).findByName(name);
        }
    }
}