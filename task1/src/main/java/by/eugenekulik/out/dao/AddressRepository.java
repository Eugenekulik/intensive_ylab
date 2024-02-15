package by.eugenekulik.out.dao;

import by.eugenekulik.model.Address;

import java.util.List;
import java.util.Optional;

/**
 * The {@code AddressRepository} interface defines methods for interacting with
 * address-related data in a repository.
 */
public interface AddressRepository {

    /**
     * Retrieves an address by its unique identifier.
     *
     * @param id The unique identifier of the address.
     * @return An {@code Optional} containing the address, or empty if not found.
     */
    Optional<Address> findById(Long id);

    /**
     * Saves an address in the repository.
     *
     * @param address The address to save.
     * @return The saved address.
     */
    Address save(Address address);

    /**
     * Retrieves a page of addresses from the repository.
     *
     * @param pageable class with information about page number and count number
     * @return A list of addresses for the specified page.
     */
    List<Address> getPage(Pageable pageable);

    List<Address> findByUserId(Long userId, Pageable pageable);
}
