package by.eugenekulik.service;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The AddressService interface defines methods for managing addresses.
 * It provides functionality to create, retrieve, and paginate addresses.
 */
public interface AddressService {

    /**
     * Creates a new address based on the provided AddressRequestDto.
     *
     * @param addressRequestDto The AddressRequestDto containing information for creating the address.
     * @return The created AddressResponseDto.
     */
    AddressResponseDto create(AddressRequestDto addressRequestDto);

    /**
     * Retrieves a paginated list of AddressRequestDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AddressResponseDto objects for the specified page.
     */
    List<AddressResponseDto> getPage(Pageable pageable);

    /**
     * Retrieves the details of an address by its unique identifier.
     *
     * @param id The unique identifier of the address.
     * @return The AddressResponseDto object for the specified ID.
     */
    AddressResponseDto findById(long id);

    /**
     * Retrieves a paginated list of AddressRequestDto objects associated with a specific user.
     *
     * @param userId   The unique identifier of the user.
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AddressResponseDto objects for the specified user and page.
     */
    List<AddressResponseDto> findByUser(Long userId, Pageable pageable);
}
