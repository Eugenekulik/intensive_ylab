package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.service.impl.AgreementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgreementServiceTest extends TestConfigurationEnvironment {

    private AgreementRepository agreementRepository;
    private AgreementService agreementService;

    @BeforeEach
    void setup(){
        agreementRepository = mock(AgreementRepository.class);
        agreementService = new AgreementServiceImpl(agreementRepository);
    }

    @Test
    void testCreate_shouldReturnAgreement_whenAddressAndUserValid(){
        Agreement agreement = Agreement.builder().userId(1L).addressId(1L).build();

        when(agreementRepository.save(agreement)).thenReturn(agreement);

        assertEquals(agreement, agreementService.create(agreement));

        verify(agreementRepository).save(agreement);
    }

    @Test
    void testCreate_shouldThrowException_whenNotValidConstraintsInRepository() {
        Agreement agreement = mock(Agreement.class);

        when(agreementRepository.save(agreement))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> agreementService.create(agreement));

        verify(agreementRepository).save(agreement);
    }




    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        List<Agreement> agreements = mock(List.class);

        when(agreementRepository.getPage(pageable)).thenReturn(agreements);

        assertEquals(agreements, agreementService.getPage(pageable));

        verify(agreementRepository).getPage(pageable);
    }

    @Test
    void testGetPage_shouldThrowException_whenNotValidPageable() {
        Pageable pageable = mock(Pageable.class);

        when(agreementRepository.getPage(pageable))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, ()->agreementService.getPage(pageable));

        verify(agreementRepository).getPage(pageable);
    }


    @Test
    void testFindByUser_whenExists(){
        List<Agreement> agreements = mock(List.class);
        Pageable pageable = mock(Pageable.class);

        when(agreementRepository.findByUserId(1L, pageable)).thenReturn(agreements);

        assertEquals(agreements, agreementService.findByUser(1L, pageable));

        verify(agreementRepository).findByUserId(1L, pageable);
    }




    @Test
    void testFindById_shouldReturnAgreement_whenIdExists() {
        Long id = 1L;
        Agreement agreement = mock(Agreement.class);

        when(agreementRepository.findById(id)).thenReturn(Optional.of(agreement));

        assertEquals(agreement, agreementService.findById(id));

        verify(agreementRepository).findById(id);
    }

    @Test
    void testFindById_shouldThrowException_whenIdDoesNotExist() {
        Long agreementId = 1L;

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> agreementService.findById(agreementId));

        verify(agreementRepository).findById(agreementId);
    }

}