package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.service.impl.AgreementServiceImpl;
import jakarta.mail.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgreementServiceTest extends TestConfigurationEnvironment {

    private AgreementRepository agreementRepository;
    private AgreementService agreementService;
    private AgreementMapper agreementMapper;

    @BeforeEach
    void setup(){
        agreementMapper = mock(AgreementMapper.class);
        agreementRepository = mock(AgreementRepository.class);
        agreementService = new AgreementServiceImpl(agreementRepository, agreementMapper);
    }

    @Test
    void testCreate_shouldReturnAgreement_whenAddressAndUserValid(){
        AgreementDto agreementDto = mock(AgreementDto.class);
        Agreement agreement = mock(Agreement.class);


        when(agreementRepository.save(agreement)).thenReturn(agreement);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementDto);
        when(agreementMapper.fromAgreementDto(agreementDto)).thenReturn(agreement);

        assertEquals(agreementDto, agreementService.create(agreementDto));

        verify(agreementRepository).save(agreement);
    }

    @Test
    void testCreate_shouldThrowException_whenNotValidConstraintsInRepository() {
        Agreement agreement = mock(Agreement.class);
        AgreementDto agreementDto = mock(AgreementDto.class);

        when(agreementRepository.save(agreement))
            .thenThrow(DatabaseInterectionException.class);
        when(agreementMapper.fromAgreementDto(agreementDto)).thenReturn(agreement);

        assertThrows(DatabaseInterectionException.class, () -> agreementService.create(agreementDto));

        verify(agreementRepository).save(agreement);
    }




    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        Agreement agreement = mock(Agreement.class);
        List<Agreement> agreements = List.of(agreement);

        when(agreementRepository.getPage(pageable)).thenReturn(agreements);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementDto);

        assertThat(agreementService.getPage(pageable))
            .hasSize(1)
                .first()
                    .isEqualTo(agreementDto);

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
        Agreement agreement = mock(Agreement.class);
        AgreementDto agreementDto = mock(AgreementDto.class);
        List<Agreement> agreements = List.of(agreement);
        Pageable pageable = mock(Pageable.class);

        when(agreementRepository.findByUserId(1L, pageable)).thenReturn(agreements);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementDto);

        assertThat(agreementService.findByUser(1L, pageable))
            .hasSize(1)
            .first()
            .isEqualTo(agreementDto);

        verify(agreementRepository).findByUserId(1L, pageable);
    }




    @Test
    void testFindById_shouldReturnAgreement_whenIdExists() {
        Long id = 1L;
        Agreement agreement = mock(Agreement.class);
        AgreementDto agreementDto = mock(AgreementDto.class);

        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementDto);
        when(agreementRepository.findById(id)).thenReturn(Optional.of(agreement));

        assertEquals(agreementDto, agreementService.findById(id));

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