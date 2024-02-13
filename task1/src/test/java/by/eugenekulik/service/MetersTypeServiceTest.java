package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.logic.MetersTypeService;
import by.eugenekulik.service.logic.impl.MetersTypeServiceImpl;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetersTypeServiceTest extends TestConfigurationEnvironment {

    private MetersTypeService metersTypeService;
    private MetersTypeRepository metersTypeRepository;

    @BeforeEach
    void setUp() {
        metersTypeRepository = mock(MetersTypeRepository.class);
        metersTypeService = new MetersTypeServiceImpl(metersTypeRepository);
    }

    @Test
    void testCreate_shouldSaveMetersType_whenNotExists() {
        MetersType metersType = mock(MetersType.class);

        when(metersTypeRepository.save(metersType)).thenReturn(metersType);

        assertEquals(metersType, metersTypeService.create(metersType));

        verify(metersTypeRepository).save(metersType);
    }

    @Test
    void testCreate_shouldThrowException_whenExists() {
        MetersType metersType = mock(MetersType.class);

        when(metersTypeRepository.save(metersType))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> metersTypeService.create(metersType));

        verify(metersTypeRepository).save(metersType);
    }

    @Test
    void testFindByName_shouldReturnMetersType_whenExists() {
        String metersTypeName = "warm_water";
        MetersType metersType = mock(MetersType.class);

        when(metersTypeRepository.findByName(metersTypeName)).thenReturn(Optional.of(metersType));

        assertEquals(metersType, metersTypeService.findByName(metersTypeName));

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
        List<MetersType> metersTypes = mock(List.class);

        when(metersTypeRepository.findAll()).thenReturn(metersTypes);

        assertEquals(metersTypes, metersTypeService.findAll());

        verify(metersTypeRepository).findAll();
    }

}