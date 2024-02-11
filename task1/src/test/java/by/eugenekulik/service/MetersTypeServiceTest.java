package by.eugenekulik.service;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.logic.MetersTypeService;
import by.eugenekulik.service.logic.impl.MetersTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MetersTypeServiceTest {

    private MetersTypeService metersTypeService;
    private MetersTypeRepository metersTypeRepository;
    private TransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        transactionManager = mock(TransactionManager.class);
        metersTypeRepository = mock(MetersTypeRepository.class);
        metersTypeService = new MetersTypeServiceImpl(metersTypeRepository, transactionManager);
    }

    @Test
    void testCreate_shouldSaveMetersType_whenNotExists() {
        MetersType metersType = mock(MetersType.class);

        when(transactionManager.doInTransaction(any()))
            .thenReturn(metersType);

        assertEquals(metersType, metersTypeService.create(metersType));

        verify(transactionManager).doInTransaction(any());
    }

    @Test
    void testCreate_shouldThrowException_whenExists() {
        MetersType metersType = mock(MetersType.class);

        when(transactionManager.doInTransaction(any()))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> metersTypeService.create(metersType));

        verify(metersTypeRepository, never()).save(any());
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
        String nonExistentMetersTypeName = "electric";

        when(metersTypeRepository.findByName(nonExistentMetersTypeName)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> metersTypeService.findByName(nonExistentMetersTypeName),
            "not found metersType with name: " + nonExistentMetersTypeName);

        verify(metersTypeRepository).findByName(nonExistentMetersTypeName);
    }

    @Test
    void testFindAll_shouldReturnAllMetersTypes() {
        List<MetersType> metersTypes = mock(List.class);

        when(metersTypeRepository.findAll()).thenReturn(metersTypes);

        assertEquals(metersTypes, metersTypeService.findAll());

        verify(metersTypeRepository).findAll();
    }

}