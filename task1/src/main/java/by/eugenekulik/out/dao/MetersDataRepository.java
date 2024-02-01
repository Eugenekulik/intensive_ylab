package by.eugenekulik.out.dao;

import by.eugenekulik.model.MetersData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The {@code MetersDataRepository} interface defines methods for interacting with
 * meters data-related data in a repository.
 */
public interface MetersDataRepository {

    /**
     * Retrieves meters data by its unique identifier.
     *
     * @param id The unique identifier of the meters data.
     * @return An {@code Optional} containing the meters data, or empty if not found.
     */
    Optional<MetersData> findById(Long id);

    /**
     * Saves meters data in the repository.
     *
     * @param metersData The meters data to save.
     * @return The saved meters data.
     */
    MetersData save(MetersData metersData);

    /**
     * Retrieves meters data by agreement ID, meters type ID, and placement date (month).
     *
     * @param agreementId  The agreement ID.
     * @param metersTypeId The meters type ID.
     * @param placedAt     The placement date (month).
     * @return An {@code Optional} containing the meters data, or empty if not found.
     */
    Optional<MetersData> findByAgreementAndTypeAndMonth(Long agreementId,
                                                        Long metersTypeId,
                                                        LocalDate placedAt);

    /**
     * Retrieves a page of meters data from the repository.
     *
     * @param page  The page number (0-indexed).
     * @param count The number of meters data entries per page.
     * @return A list of meters data for the specified page.
     */
    List<MetersData> getPage(int page, int count);

    /**
     * Retrieves a page of meters data by agreement ID from the repository.
     *
     * @param agreementId The agreement ID.
     * @param page        The page number (0-indexed).
     * @param count       The number of meters data entries per page.
     * @return A list of meters data for the specified agreement and page.
     */
    List<MetersData> getPageByAgreement(Long agreementId, int page, int count);

    /**
     * Retrieves the last meters data entry by agreement ID and meters type ID.
     *
     * @param agreementId  The agreement ID.
     * @param metersTypeId The meters type ID.
     * @return An {@code Optional} containing the last meters data entry, or empty if not found.
     */
    Optional<MetersData> findLastByAgreementAndType(Long agreementId, Long metersTypeId);
}
