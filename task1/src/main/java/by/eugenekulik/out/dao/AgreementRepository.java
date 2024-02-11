package by.eugenekulik.out.dao;

import by.eugenekulik.model.Agreement;

import java.util.List;
import java.util.Optional;

/**
 * The {@code AgreementRepository} interface defines methods for interacting with agreement-related data in a repository.
 */
public interface AgreementRepository {

    /**
     * Retrieves an agreement by its unique identifier.
     *
     * @param id The unique identifier of the agreement.
     * @return An {@code Optional} containing the agreement, or empty if not found.
     */
    Optional<Agreement> findById(Long id);

    /**
     * Saves an agreement in the repository.
     *
     * @param agreement The agreement to save.
     * @return The saved agreement.
     */
    Agreement save(Agreement agreement);

    /**
     * Retrieves agreements by user ID and address ID.
     *
     * @param userId    The user ID.
     * @param addressId The address ID.
     * @return A list of agreements matching the user ID and address ID.
     */
    List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId);

    /**
     * Retrieves a page of agreements from the repository.
     *
     * @param pageable class with information about page number and count number
     * @return A list of agreements for the specified page.
     */
    List<Agreement> getPage(Pageable pageable);

    /**
     * Retrieves agreements by user ID.
     *
     * @param userId   The user ID.
     * @param pageable class with information about page number and count number
     * @return A list of agreements for the specified user.
     */
    List<Agreement> findByUserId(Long userId, Pageable pageable);
}