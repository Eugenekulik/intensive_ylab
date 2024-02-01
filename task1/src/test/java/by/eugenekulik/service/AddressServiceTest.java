package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {


    private AddressRepository addressRepository;
    private AddressService addressService;
    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        addressService = new AddressService(addressRepository);
    }

    @Test
    void testCreate_shouldSave_whenNotExists(){
        Address address = mock(Address.class);

        when(addressRepository.save(address)).thenReturn(address);
        when(addressRepository.isPresent(address)).thenReturn(false);

        assertEquals(address, addressService.create(address));

        verify(addressRepository).save(address);
        verify(addressRepository).isPresent(address);
    }


    @Test
    void testCreate_shouldThrowException_whenExists(){
        Address address = mock(Address.class);

        when(addressRepository.isPresent(address)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()->addressService.create(address));

        verify(addressRepository).isPresent(address);
        verify(addressRepository, never()).save(address);
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        int page = 1;
        int count = 10;
        List<Address> expectedAddresses = mock(List.class);

        when(addressRepository.getPage(page, count)).thenReturn(expectedAddresses);

        assertEquals(expectedAddresses, addressService.getPage(page, count));

        verify(addressRepository).getPage(page, count);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageIsOutOfRange() {
        int page = -1;
        int count = 10;

        assertThrows(IllegalArgumentException.class, ()->addressService.getPage(page, count));

        verify(addressRepository, never()).getPage(page, count);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenCountIsNegative() {
        int page = 1;
        int count = -5;

        assertThrows(IllegalArgumentException.class, ()->addressService.getPage(page, count));

        verify(addressRepository, never()).getPage(page,count);
    }






}