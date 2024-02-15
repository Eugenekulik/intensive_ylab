package by.eugenekulik.service;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;

/**
 * The AddressService interface defines methods for managing addresses.
 * It provides functionality to create, retrieve, and paginate addresses.
 */
public interface AddressService {

    /**
     * Creates a new address based on the provided AddressDto.
     *
     * @param addressDto The AddressDto containing information for creating the address.
     * @return The created AddressDto.
     */
    AddressDto create(AddressDto addressDto);

    /**
     * Retrieves a paginated list of AddressDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AddressDto objects for the specified page.
     */
    List<AddressDto> getPage(Pageable pageable);

    /**
     * Retrieves the details of an address by its unique identifier.
     *
     * @param id The unique identifier of the address.
     * @return The AddressDto object for the specified ID.
     */
    AddressDto findById(long id);

    /**
     * Retrieves a paginated list of AddressDto objects associated with a specific user.
     *
     * @param userId   The unique identifier of the user.
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AddressDto objects for the specified user and page.
     */
    List<AddressDto> findByUser(Long userId, Pageable pageable);
}
