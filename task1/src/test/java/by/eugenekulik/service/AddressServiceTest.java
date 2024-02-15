package by.eugenekulik.service;

import by.eugenekulik.TestConfigurationEnvironment;
import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.service.impl.AddressServiceImpl;
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
    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        addressRepository = mock(AddressRepository.class);
        addressMapper = mock(AddressMapper.class);
        addressService = new AddressServiceImpl(addressRepository, addressMapper);
    }

    @Test
    void testCreate_shouldSave_whenNotExists() {
        Address address = mock(Address.class);
        AddressDto addressDto = mock(AddressDto.class);

        when(addressMapper.fromAddressDto(addressDto)).thenReturn(address);
        when(addressRepository.save(address))
            .thenReturn(address);
        when(addressMapper.fromAddress(address)).thenReturn(addressDto);

        assertEquals(addressDto, addressService.create(addressDto));

        verify(addressRepository).save(address);
    }


    @Test
    void testCreate_shouldThrowException_whenExists() {
        AddressDto addressDto = mock(AddressDto.class);
        Address address = mock(Address.class);

        when(addressMapper.fromAddressDto(addressDto)).thenReturn(address);
        when(addressRepository.save(address))
            .thenThrow(DatabaseInterectionException.class);

        assertThrows(DatabaseInterectionException.class, () -> addressService.create(addressDto));

        verify(addressRepository).save(address);
    }


    @Test
    void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
        Pageable pageable = mock(Pageable.class);
        Address address = mock(Address.class);
        AddressDto addressDto = mock(AddressDto.class);
        List<Address> addresses = List.of(address);

        when(addressRepository.getPage(pageable)).thenReturn(addresses);
        when(addressMapper.fromAddress(address))
            .thenReturn(addressDto);

        assertThat(addressService.getPage(pageable))
            .hasSize(1)
            .first()
            .isEqualTo(addressDto);

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
        AddressDto addressDto = mock(AddressDto.class);

        when(addressRepository.findById(1L))
            .thenReturn(Optional.of(address));
        when(addressMapper.fromAddress(address)).thenReturn(addressDto);

        assertEquals(addressDto, addressService.findById(1L));

    }


}