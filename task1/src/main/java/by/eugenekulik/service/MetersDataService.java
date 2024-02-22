package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The MetersDataService interface defines methods for managing meters data.
 * It provides functionality to create, retrieve, and paginate meters data.
 */
public interface MetersDataService {

    /**
     * Creates a new meters data entry based on the provided MetersDataRequestDto.
     *
     * @param metersDataRequestDto The MetersDataRequestDto containing information for creating the meters data entry.
     * @return The created MetersDataResponseDto.
     */
    MetersDataResponseDto create(MetersDataRequestDto metersDataRequestDto);

    /**
     * Retrieves the last meters data entry for a specific agreement and meters type.
     *
     * @param agreementId The unique identifier of the agreement.
     * @param typeName    The name of the meters type.
     * @return The last MetersDataResponseDto for the specified agreement and meters type.
     */
    MetersDataResponseDto findLastByAgreementAndType(Long agreementId, String typeName);

    /**
     * Retrieves a paginated list of MetersDataRequestDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of MetersDataResponseDto objects for the specified page.
     */
    List<MetersDataResponseDto> getPage(Pageable pageable);

    /**
     * Retrieves a paginated list of MetersDataRequestDto objects for a specific agreement and meters type.
     *
     * @param agreementId The unique identifier of the agreement.
     * @param typeName    The name of the meters type.
     * @param pageable    The Pageable object specifying the number of results and offset.
     * @return A List of MetersDataResponseDto objects for the specified agreement, meters type, and page.
     */
    List<MetersDataResponseDto> findByAgreementAndType(long agreementId, String typeName, Pageable pageable);
}

