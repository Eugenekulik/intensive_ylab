package by.eugenekulik.out.dao.jdbc;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcAddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        void testSave_shouldSaveAndReturnAddress(){
            Address address = Address.builder()
                .region("Grodno region")
                .district("Volkovysk district")
                .city("Volkovysk")
                .street("Gorbatovo")
                .house("9")
                .apartment("2")
                .build();

            assertThat(addressRepository.save(address))
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("region", "Grodno region");
        }

        @Test
        void testFindById_shouldReturnOptionalOfAddress_whenAddressExists(){
            assertTrue(addressRepository.findById(1L).isPresent());
        }

        @Test
        void testGetPage_shouldReturnListOfAddresses_whenPageAndCountValid(){
            Pageable pageable = PageRequest.of(0,2);
            List<Address> result = addressRepository.getPage(pageable);

            assertThat(result)
                .anyMatch(address -> address.getRegion().equals("Minsk region"));
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        void testFindById_shouldReturnEmptyOptional_whenAddressNotExists(){
            assertTrue(addressRepository.findById(1000L).isEmpty());
        }

        @Test
        void testGetPage_shouldReturnEmptyList_whenPageOutOfBound(){
            Pageable pageable = PageRequest.of(4,4);

            List<Address> result = addressRepository.getPage(pageable);

            assertThat(result).isEmpty();
        }
    }
















}