package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.service.AgreementMapper;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import by.eugenekulik.service.AgreementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Service class for handling operations related to agreements.
 * Manages the interaction with the underlying AgreementRepository, AddressRepository, and UserRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private AgreementRepository agreementRepository;
    private AgreementMapper agreementMapper;

    /**
     * Constructs an AgreementService with the specified repositories.
     *
     * @param agreementRepository The repository responsible for managing agreements.
     * @param agreementMapper The mapper for agreement model.
     */
    @Inject
    public AgreementServiceImpl(AgreementRepository agreementRepository, AgreementMapper agreementMapper) {
        this.agreementRepository = agreementRepository;
        this.agreementMapper = agreementMapper;
    }

    /**
     * Creates a new agreement if the associated user and address exist,
     * and if an agreement with the same user and address doesn't already exist.
     *
     * @param agreementDto The information of agreement to be created.
     * @return The created agreement.
     * @throws IllegalArgumentException If the associated user or address doesn't exist,
     *                                  or if an agreement with the same user and address already exists.
     */
    @Override
    @Timed
    @Transactional
    public AgreementDto create(AgreementDto agreementDto) {
        return agreementMapper
            .fromAgreement(agreementRepository
                .save(agreementMapper
                    .fromAgreementDto(agreementDto)));
    }

    /**
     * Retrieves a paginated list of agreements.
     *
     * @param pageable class with information about page number and count number
     * @return A list of agreements for the specified page and count.
     * @throws IllegalArgumentException If page is negative or if count is less than 1.
     */
    @Override
    @Timed
    public List<AgreementDto> getPage(Pageable pageable) {
        return agreementRepository.getPage(pageable)
            .stream()
            .map(agreementMapper::fromAgreement)
            .toList();
    }

    /**
     * Retrieves a list of agreements associated with the specified user ID.
     *
     * @param userId The ID of the user.
     * @return A list of agreements associated with the specified user ID.
     */
    @Override
    @Timed
    public List<AgreementDto> findByUser(Long userId, Pageable pageable) {
        return agreementRepository.findByUserId(userId, pageable)
            .stream()
            .map(agreementMapper::fromAgreement)
            .toList();
    }

    /**
     * Retrieves an agreement by its ID.
     *
     * @param agreementId The ID of the agreement.
     * @return The agreement with the specified ID.
     * @throws IllegalArgumentException If no agreement is found with the specified ID.
     */
    @Override
    @Timed
    public AgreementDto findById(Long agreementId) {
        return agreementRepository.findById(agreementId)
            .map(agreementMapper::fromAgreement)
            .orElseThrow(() -> new IllegalArgumentException("Not found agreement with id: " + agreementId));
    }

}
