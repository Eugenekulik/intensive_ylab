package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.service.AddressMapper;
import by.eugenekulik.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@UnitTest
@WebAppConfiguration
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testCreate_shouldSave_whenNotExists() {
            Address address = mock(Address.class);
            AddressRequestDto addressRequestDto = mock(AddressRequestDto.class);
            AddressResponseDto addressResponseDto = mock(AddressResponseDto.class);

            when(addressMapper.fromAddressDto(addressRequestDto)).thenReturn(address);
            when(addressRepository.save(address))
                .thenReturn(address);
            when(addressMapper.fromAddress(address)).thenReturn(addressResponseDto);

            assertEquals(addressResponseDto, addressService.create(addressRequestDto));

            verify(addressRepository).save(address);
        }

        @Test
        void testGetPage_shouldReturnCorrectPage_whenPageAndCountAreValid() {
            Pageable pageable = mock(Pageable.class);
            Address address = mock(Address.class);
            AddressResponseDto addressResponseDto = mock(AddressResponseDto.class);
            List<Address> addresses = List.of(address);

            when(addressRepository.getPage(pageable)).thenReturn(addresses);
            when(addressMapper.fromAddress(address))
                .thenReturn(addressResponseDto);

            assertThat(addressService.getPage(pageable))
                .hasSize(1)
                .first()
                .isEqualTo(addressResponseDto);

            verify(addressRepository).getPage(pageable);
        }

        @Test
        void testFindById_shouldReturnAddress_whenExists() {
            Address address = mock(Address.class);
            AddressResponseDto addressResponseDto = mock(AddressResponseDto.class);

            when(addressRepository.findById(1L))
                .thenReturn(Optional.of(address));
            when(addressMapper.fromAddress(address)).thenReturn(addressResponseDto);

            assertEquals(addressResponseDto, addressService.findById(1L));

        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testCreate_shouldThrowException_whenExists() {
            AddressRequestDto addressRequestDto = mock(AddressRequestDto.class);
            Address address = mock(Address.class);

            when(addressMapper.fromAddressDto(addressRequestDto)).thenReturn(address);
            when(addressRepository.save(address))
                .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> addressService.create(addressRequestDto));

            verify(addressRepository).save(address);
        }
    }
}