package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.logic.AddressService;
import by.eugenekulik.service.logic.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest extends TestConfigurationEnvironment {


    private AddressRepository addressRepository;

    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        addressService = new AddressServiceImpl(addressRepository);
    }

    @Test
    void testCreate_shouldSave_whenNotExists() {
        Address address = mock(Address.class);

        when(addressRepository.save(address))
            .thenReturn(address);

        assertEquals(address, addressService.create(address));

        verify(addressRepository).save(address);
    }


    @Test
    void testCreate_shouldThrowException_whenExists() {
        Address address = mock(Address.class);

        when(addressRepository.save(address))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> addressService.create(address));

        verify(addressRepository).save(address);
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        List<Address> expectedAddresses = mock(List.class);

        when(addressRepository.getPage(pageable)).thenReturn(expectedAddresses);

        assertEquals(expectedAddresses, addressService.getPage(pageable));

        verify(addressRepository).getPage(pageable);
    }

    @Test
    void testGetPage_shouldReturnEmptyList_whenPageIsOutOfRange() {
        Pageable pageable = mock(Pageable.class);
        when(addressRepository.getPage(pageable))
            .thenReturn(Collections.EMPTY_LIST);

        assertThat(addressService.getPage(pageable))
            .isEmpty();


    }

    @Test
    void testGetPage_shouldThrowException_whenCountIsNegative() {
        Pageable pageable = mock(Pageable.class);
        when(addressRepository.getPage(pageable))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> addressService.getPage(pageable));
    }


    @Test
    void testFindById_shouldReturnAddress_whenExists() {
        Address address = mock(Address.class);

        when(addressRepository.findById(1L))
            .thenReturn(Optional.of(address));

        assertEquals(address, addressService.findById(1L));

    }


}