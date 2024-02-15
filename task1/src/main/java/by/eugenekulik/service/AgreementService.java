package by.eugenekulik.service;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;

/**
 * The AgreementService interface defines methods for managing agreements.
 * It provides functionality to create, retrieve, and paginate agreements.
 */
public interface AgreementService {

    /**
     * Creates a new agreement based on the provided AgreementDto.
     *
     * @param agreementDto The AgreementDto containing information for creating the agreement.
     * @return The created AgreementDto.
     */
    AgreementDto create(AgreementDto agreementDto);

    /**
     * Retrieves a paginated list of AgreementDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AgreementDto objects for the specified page.
     */
    List<AgreementDto> getPage(Pageable pageable);

    /**
     * Retrieves a paginated list of AgreementDto objects associated with a specific user.
     *
     * @param userId   The unique identifier of the user.
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of AgreementDto objects for the specified user and page.
     */
    List<AgreementDto> findByUser(Long userId, Pageable pageable);

    /**
     * Retrieves the details of an agreement by its unique identifier.
     *
     * @param agreementId The unique identifier of the agreement.
     * @return The AgreementDto object for the specified agreement ID.
     */
    AgreementDto findById(Long agreementId);
}

