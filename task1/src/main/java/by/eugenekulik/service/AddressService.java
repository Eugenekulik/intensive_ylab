package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;

import java.util.List;

/**
 * Service class for handling operations related to addresses.
 * Manages the interaction with the underlying AddressRepository.
 * Provides methods for creating addresses and retrieving paginated lists of addresses.
 *
 * @author Eugene Kulik
 */
public class AddressService {


    private final AddressRepository addressRepository;


    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository The repository responsible for managing addresses.
     */
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    /**
     * Creates a new address if it doesn't already exist.
     *
     * @param address The address to be created.
     * @return The created address.
     * @throws IllegalArgumentException If the address already exists.
     */
    public Address create(Address address) {
        if (addressRepository.isPresent(address)) {
            throw new IllegalArgumentException("This address already exists");
        }
        return addressRepository.save(address);
    }


    /**
     * Retrieves a paginated list of addresses.
     *
     * @param page  The page number (starting from 0).
     * @param count The number of addresses to retrieve per page.
     * @return A list of addresses for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    public List<Address> getPage(int page, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be more then 0");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        return addressRepository.getPage(page, count);
    }
}
