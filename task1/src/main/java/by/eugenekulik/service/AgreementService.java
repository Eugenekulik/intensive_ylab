package by.eugenekulik.service;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The AgreementService interface defines methods for managing agreements.
 * It provides functionality to create, retrieve, and paginate agreements.
 */
public interface AgreementService {

    /**
     * Creates a new agreement based on the provided AgreementRequestDto.
     *
     * @param agreementRequestDto The AgreementRequestDto containing information for creating the agreement.
     * @return The created AgreementResponseDto.
     */
    AgreementResponseDto create(AgreementRequestDto agreementRequestDto);

    /**
     * Retrieves a paginated list of AgreementRequestDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AgreementResponseDto objects for the specified page.
     */
    List<AgreementResponseDto> getPage(Pageable pageable);

    /**
     * Retrieves a paginated list of AgreementRequestDto objects associated with a specific user.
     *
     * @param userId   The unique identifier of the user.
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AgreementResponseDto objects for the specified user and page.
     */
    List<AgreementResponseDto> findByUser(Long userId, Pageable pageable);

    /**
     * Retrieves the details of an agreement by its unique identifier.
     *
     * @param agreementId The unique identifier of the agreement.
     * @return The AgreementResponseDto object for the specified agreement ID.
     */
    AgreementResponseDto findById(Long agreementId);
}

