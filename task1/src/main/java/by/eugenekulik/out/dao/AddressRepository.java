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
     * Checks if an address is present in the repository.
     *
     * @param address The address to check.
     * @return {@code true} if the address is present, {@code false} otherwise.
     */
    boolean isPresent(Address address);

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
     * @param page  The page number (0-indexed).
     * @param count The number of addresses per page.
     * @return A list of addresses for the specified page.
     */
    List<Address> getPage(int page, int count);
}
