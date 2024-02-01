package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgreementServiceTest {

    private AddressRepository addressRepository;
    private AgreementRepository agreementRepository;
    private UserRepository userRepository;
    private AgreementService agreementService;

    @BeforeEach
    void setup(){
        addressRepository = mock(AddressRepository.class);
        agreementRepository = mock(AgreementRepository.class);
        userRepository = mock(UserRepository.class);
        agreementService = new AgreementService(agreementRepository, addressRepository, userRepository);
    }

    @Test
    void testCreate_shouldReturnAgreement_whenAddressAndUserValid(){
        Agreement agreement = Agreement.builder().userId(1L).addressId(1L).build();
        User user = User.builder().id(1L).build();
        Address address = Address.builder().id(1L).build();

        when(userRepository.findById(agreement.getUserId())).thenReturn(Optional.of(user));
        when(addressRepository.findById(agreement.getAddressId())).thenReturn(Optional.of(address));
        when(agreementRepository.save(agreement)).thenReturn(agreement);

        assertEquals(agreement, agreementService.create(agreement));

        verify(userRepository).findById(user.getId());
        verify(addressRepository).findById(address.getId());
        verify(agreementRepository).save(agreement);
    }

    @Test
    void testCreate_shouldThrowException_whenAddressNotFound() {
        Agreement agreement = mock(Agreement.class);

        when(addressRepository.findById(agreement.getAddressId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> agreementService.create(agreement));

        verify(userRepository, never()).findById(any());
        verify(agreementRepository, never()).findByUserIdAndAddressId(any(), any());
        verify(agreementRepository, never()).save(any());
    }

    @Test
    void testCreate_shouldThrowException_whenUserNotFound() {
        Agreement agreement = mock(Agreement.class);

        when(addressRepository.findById(agreement.getAddressId())).thenReturn(Optional.of(new Address()));
        when(userRepository.findById(agreement.getUserId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> agreementService.create(agreement));

        verify(agreementRepository, never()).findByUserIdAndAddressId(any(), any());
        verify(agreementRepository, never()).save(any());
    }

    @Test
    void testCreate_shouldThrowException_whenAgreementAlreadyExists() {
        Agreement agreement = mock(Agreement.class);

        when(addressRepository.findById(agreement.getAddressId())).thenReturn(Optional.of(new Address()));
        when(userRepository.findById(agreement.getUserId())).thenReturn(Optional.of(new User()));
        when(agreementRepository.findByUserIdAndAddressId(agreement.getUserId(), agreement.getAddressId()))
            .thenReturn(List.of(new Agreement()));

        assertThrows(IllegalArgumentException.class, () -> agreementService.create(agreement));

        verify(agreementRepository).findByUserIdAndAddressId(agreement.getUserId(), agreement.getAddressId());
        verify(agreementRepository, never()).save(any());
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        int page = 1;
        int count = 10;
        List<Agreement> agreements = mock(List.class);

        when(agreementRepository.getPage(page, count)).thenReturn(agreements);

        assertEquals(agreements, agreementService.getPage(page, count));

        verify(agreementRepository).getPage(page, count);
    }

    @Test
    void testGetPage_shouldThrowException_whenPageIsNegative() {
        int page = -1;
        int count = 10;

        assertThrows(IllegalArgumentException.class, ()->agreementService.getPage(page,count));

        verify(agreementRepository, never()).getPage(anyInt(), anyInt());
    }

    @Test
    void getPage_shouldReturnEmptyList_whenCountLessByOne() {
        int page = 1;
        int count = -5;

        assertThrows(IllegalArgumentException.class, ()->agreementService.getPage(page, count));

        verify(agreementRepository, never()).getPage(page, count);
    }


    @Test
    void testFindByUserSuccessful(){
        List<Agreement> agreements = mock(List.class);

        when(agreementRepository.findByUserId(1L)).thenReturn(agreements);

        assertEquals(agreements, agreementService.findByUser(1L));

        verify(agreementRepository).findByUserId(1L);
    }

    @Test
    void testFindByUser_shouldReturnAgreementsForUser_whenUserExists() {
        Long userId = 1L;
        List<Agreement> agreements = mock(List.class);

        when(agreementRepository.findByUserId(userId)).thenReturn(agreements);

        assertEquals(agreements, agreementService.findByUser(userId));

        verify(agreementRepository).findByUserId(userId);
    }


    @Test
    void testFindById_shouldReturnAgreement_whenIdExists() {
        Long agreementId = 1L;
        Agreement agreement = mock(Agreement.class);

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.of(agreement));

        assertEquals(agreement, agreementService.findById(agreementId));

        verify(agreementRepository).findById(agreementId);
    }

    @Test
    void testFindById_shouldThrowException_whenIdDoesNotExist() {
        Long agreementId = 1L;

        when(agreementRepository.findById(agreementId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> agreementService.findById(agreementId));

        verify(agreementRepository).findById(agreementId);
    }

}