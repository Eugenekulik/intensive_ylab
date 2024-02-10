package by.eugenekulik.service;

import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {


    private AddressRepository addressRepository;

    private TransactionManager transactionManager;
    private AddressService addressService;
    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        transactionManager = mock(TransactionManager.class);
        addressService = new AddressServiceImpl(addressRepository, transactionManager);
    }

    @Test
    void testCreate_shouldSave_whenNotExists(){
        Address address = mock(Address.class);

        when(transactionManager.doInTransaction(any()))
            .thenReturn(address);

        assertEquals(address, addressService.create(address));

        verify(transactionManager).doInTransaction(any());
    }


    @Test
    void testCreate_shouldThrowException_whenExists(){
        Address address = mock(Address.class);

        when(transactionManager.doInTransaction(any()))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, ()->addressService.create(address));

        verify(transactionManager).doInTransaction(any());
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
        int page = 4;
        int count = 10;

        when(addressRepository.getPage(page, count))
            .thenReturn(Collections.EMPTY_LIST);

        assertThat(addressService.getPage(page, count))
            .isEmpty();


    }

    @Test
    void testGetPage_shouldThrowException_whenCountIsNegative() {
        int page = 1;
        int count = -5;

        when(addressRepository.getPage(page, count))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, ()->addressService.getPage(page, count));
    }






}