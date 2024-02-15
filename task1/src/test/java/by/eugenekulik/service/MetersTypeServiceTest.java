package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.impl.MetersTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetersTypeServiceTest extends TestConfigurationEnvironment {

    private MetersTypeService metersTypeService;
    private MetersTypeRepository metersTypeRepository;
    private MetersTypeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = mock(MetersTypeMapper.class);
        metersTypeRepository = mock(MetersTypeRepository.class);
        metersTypeService = new MetersTypeServiceImpl(metersTypeRepository, mapper);
    }

    @Test
    void testCreate_shouldSaveMetersType_whenNotExists() {
        MetersType metersType = mock(MetersType.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);

        when(metersTypeRepository.save(metersType)).thenReturn(metersType);
        when(mapper.fromMetersTypeDto(metersTypeDto)).thenReturn(metersType);
        when(mapper.fromMetersType(metersType)).thenReturn(metersTypeDto);

        assertEquals(metersTypeDto, metersTypeService.create(metersTypeDto));

        verify(metersTypeRepository).save(metersType);
    }

    @Test
    void testCreate_shouldThrowException_whenExists() {
        MetersType metersType = mock(MetersType.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);

        when(mapper.fromMetersTypeDto(metersTypeDto)).thenReturn(metersType);
        when(metersTypeRepository.save(metersType))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> metersTypeService.create(metersTypeDto));

        verify(metersTypeRepository).save(metersType);
    }

    @Test
    void testFindByName_shouldReturnMetersType_whenExists() {
        String metersTypeName = "warm_water";
        MetersType metersType = mock(MetersType.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);

        when(mapper.fromMetersType(metersType)).thenReturn(metersTypeDto);
        when(metersTypeRepository.findByName(metersTypeName)).thenReturn(Optional.of(metersType));

        assertEquals(metersTypeDto, metersTypeService.findByName(metersTypeName));

        verify(metersTypeRepository).findByName(metersTypeName);
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

    @Test
    void testFindAll_shouldReturnAllMetersTypes() {
        MetersType metersType = mock(MetersType.class);
        MetersTypeDto metersTypeDto = mock(MetersTypeDto.class);
        List<MetersType> metersTypes = List.of(metersType);

        when(mapper.fromMetersType(metersType)).thenReturn(metersTypeDto);
        when(metersTypeRepository.findAll()).thenReturn(metersTypes);

        assertThat(metersTypeService.findAll())
            .hasSize(1)
                .first()
                    .isEqualTo(metersTypeDto);

        verify(metersTypeRepository).findAll();
    }

}