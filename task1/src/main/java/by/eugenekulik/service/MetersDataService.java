package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The MetersDataService interface defines methods for managing meters data.
 * It provides functionality to create, retrieve, and paginate meters data.
 */
public interface MetersDataService {

    /**
     * Creates a new meters data entry based on the provided MetersDataDto.
     *
     * @param metersDataDto The MetersDataDto containing information for creating the meters data entry.
     * @return The created MetersDataDto.
     */
    MetersDataDto create(MetersDataDto metersDataDto);

    /**
     * Retrieves the last meters data entry for a specific agreement and meters type.
     *
     * @param agreementId The unique identifier of the agreement.
     * @param typeName    The name of the meters type.
     * @return The last MetersDataDto for the specified agreement and meters type.
     */
    MetersDataDto findLastByAgreementAndType(Long agreementId, String typeName);

    /**
     * Retrieves a paginated list of MetersDataDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of MetersDataDto objects for the specified page.
     */
    List<MetersDataDto> getPage(Pageable pageable);

    /**
     * Retrieves a paginated list of MetersDataDto objects for a specific agreement and meters type.
     *
     * @param agreementId The unique identifier of the agreement.
     * @param typeName    The name of the meters type.
     * @param pageable    The Pageable object specifying the number of results and offset.
     * @return A List of MetersDataDto objects for the specified agreement, meters type, and page.
     */
    List<MetersDataDto> findByAgreementAndType(long agreementId, String typeName, Pageable pageable);
}

