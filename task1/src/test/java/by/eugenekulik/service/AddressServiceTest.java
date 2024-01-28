package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AddressServiceTest {


    private AddressRepository addressRepository;
    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
    }

    @Test
    void testCreate(){
        AddressService addressService = new AddressService(addressRepository);
        Address address = mock(Address.class);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressRepository.isPresent(address)).thenReturn(false);
        assertEquals(address, addressService.create(address));
    }









}