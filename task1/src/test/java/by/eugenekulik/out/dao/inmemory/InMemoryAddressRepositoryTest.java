package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.utils.IncrementSequence;

import org.junit.jupiter.api.*;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryAddressRepositoryTest {

    private static AddressRepository addressRepository;

    @BeforeAll
    static void setUp(){
        addressRepository = new InMemoryAddressRepository(new IncrementSequence(1L, 1L));
    }



    @Test
    @Order(1)
    void testSave_shouldSaveAndReturnAddress(){
        Address address = Address.builder()
            .region("minsk")
            .district("minsk")
            .city("minsk")
            .street("baikalskaya")
            .house("61")
            .apartment("42")
            .build();

        assertEquals(address, addressRepository.save(address));
        assertEquals(address.getId(), 1L);
    }

    @Test
    @Order(2)
    void testFindById_shouldReturnOptionalOfAddress_whenAddressExists(){
        assertTrue(addressRepository.findById(1L).isPresent());
    }

    @Test
    void testFindById_shouldReturnEmptyOptional_whenAddressNotExists(){
        assertTrue(addressRepository.findById(1000L).isEmpty());
    }

    @Test
    @Order(2)
    void testIsPresent_shouldReturnTrue_whenExistAddressWithTheSameValuesOfField(){
        Address address = Address.builder()
            .region("minsk")
            .district("minsk")
            .city("minsk")
            .street("baikalskaya")
            .house("61")
            .apartment("42")
            .build();

        assertTrue(addressRepository.isPresent(address));
    }

    @Test
    @Order(2)
    void testIsPresent_shouldReturnFalse_whenNotExistAddressWithTheSameValuesOfField(){
        Address address = Address.builder()
            .region("minsk")
            .district("minsk")
            .city("minsk")
            .street("nezavisimosti")
            .house("61")
            .apartment("42")
            .build();

        assertFalse(addressRepository.isPresent(address));
    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnListOfAddresses_whenPageAndCountValid(){
        int page = 0;
        int count = 1;

        List<Address> result = addressRepository.getPage(page, count);

        assertThat(result)
            .hasSize(1)
            .anyMatch(a->a.getId().equals(1L))
            .anyMatch(a->a.getCity().equals("minsk"));

    }

    @Test
    @Order(2)
    void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
        int page = 1;
        int count = 1;

        List<Address> result = addressRepository.getPage(page, count);

        assertThat(result).isEmpty();
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenPageNegative(){
        int page = -1;
        int count = 1;

        assertThrows(IllegalArgumentException.class, ()->addressRepository.getPage(page, count));
    }

    @Test
    @Order(2)
    void testGetPage_shouldThrowException_whenCountNegative(){
        int page = 0;
        int count = -1;

        assertThrows(IllegalArgumentException.class, ()->addressRepository.getPage(page, count));
    }


}