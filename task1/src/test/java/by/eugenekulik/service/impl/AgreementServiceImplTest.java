package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.service.AgreementMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class AgreementServiceImplTest {
    @InjectMocks
    private AgreementServiceImpl agreementService;
    @Mock
    private AgreementRepository agreementRepository;
    @Mock
    private AgreementMapper agreementMapper;


    @Test
    void testCreate_shouldReturnAgreement_whenAddressAndUserValid(){
        AgreementRequestDto agreementRequestDto = mock(AgreementRequestDto.class);
        Agreement agreement = mock(Agreement.class);
        AgreementResponseDto agreementResponseDto = mock(AgreementResponseDto.class);


        when(agreementRepository.save(agreement)).thenReturn(agreement);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementResponseDto);
        when(agreementMapper.fromAgreementDto(agreementRequestDto)).thenReturn(agreement);

        assertEquals(agreementResponseDto, agreementService.create(agreementRequestDto));

        verify(agreementRepository).save(agreement);
    }

    @Test
    void testCreate_shouldThrowException_whenNotValidConstraintsInRepository() {
        Agreement agreement = mock(Agreement.class);
        AgreementRequestDto agreementRequestDto = mock(AgreementRequestDto.class);

        when(agreementRepository.save(agreement))
            .thenThrow(RuntimeException.class);
        when(agreementMapper.fromAgreementDto(agreementRequestDto)).thenReturn(agreement);

        assertThrows(RuntimeException.class, () -> agreementService.create(agreementRequestDto));

        verify(agreementRepository).save(agreement);
    }




    @Test
    void testGetPage_shouldReturnCorrectPage_whenValidPageable() {
        Pageable pageable = mock(Pageable.class);
        AgreementResponseDto agreementResponseDto = mock(AgreementResponseDto.class);
        Agreement agreement = mock(Agreement.class);
        List<Agreement> agreements = List.of(agreement);

        when(agreementRepository.getPage(pageable)).thenReturn(agreements);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementResponseDto);

        assertThat(agreementService.getPage(pageable))
            .hasSize(1)
                .first()
                    .isEqualTo(agreementResponseDto);

        verify(agreementRepository).getPage(pageable);
    }



    @Test
    void testFindByUser_whenExists(){
        Agreement agreement = mock(Agreement.class);
        AgreementResponseDto agreementResponseDto = mock(AgreementResponseDto.class);
        List<Agreement> agreements = List.of(agreement);
        Pageable pageable = PageRequest.ofSize(1);

        when(agreementRepository.findByUserId(1L, pageable)).thenReturn(agreements);
        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementResponseDto);

        assertThat(agreementService.findByUser(1L, pageable))
            .hasSize(1)
            .first()
            .isEqualTo(agreementResponseDto);

        verify(agreementRepository).findByUserId(1L, pageable);
    }




    @Test
    void testFindById_shouldReturnAgreement_whenIdExists() {
        Long id = 1L;
        Agreement agreement = mock(Agreement.class);
        AgreementResponseDto agreementResponseDto = mock(AgreementResponseDto.class);

        when(agreementMapper.fromAgreement(agreement)).thenReturn(agreementResponseDto);
        when(agreementRepository.findById(id)).thenReturn(Optional.of(agreement));

        assertEquals(agreementResponseDto, agreementService.findById(id));

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