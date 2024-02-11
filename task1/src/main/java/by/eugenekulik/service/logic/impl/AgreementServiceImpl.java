package by.eugenekulik.service.logic.impl;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.Timed;
import by.eugenekulik.service.logic.AgreementService;
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
@Timed
public class AgreementServiceImpl implements AgreementService {

    private AgreementRepository agreementRepository;
    private TransactionManager transactionManager;


    /**
     * Constructs an AgreementService with the specified repositories.
     *
     * @param agreementRepository The repository responsible for managing agreements.
     * @param transactionManager  It is needed to wrap certain database interaction logic in a transaction.
     */
    @Inject
    public AgreementServiceImpl(AgreementRepository agreementRepository, TransactionManager transactionManager) {
        this.agreementRepository = agreementRepository;

        this.transactionManager = transactionManager;
    }

    /**
     * Creates a new agreement if the associated user and address exist,
     * and if an agreement with the same user and address doesn't already exist.
     *
     * @param agreement The agreement to be created.
     * @return The created agreement.
     * @throws IllegalArgumentException If the associated user or address doesn't exist,
     *                                  or if an agreement with the same user and address already exists.
     */
    @Override
    public Agreement create(Agreement agreement) {
        return transactionManager.doInTransaction(() -> agreementRepository.save(agreement));
    }

    /**
     * Retrieves a paginated list of agreements.
     *
     * @param pageable class with information about page number and count number
     * @return A list of agreements for the specified page and count.
     * @throws IllegalArgumentException If page is negative or if count is less than 1.
     */
    @Override
    public List<Agreement> getPage(Pageable pageable) {
        return agreementRepository.getPage(pageable);
    }

    /**
     * Retrieves a list of agreements associated with the specified user ID.
     *
     * @param userId The ID of the user.
     * @return A list of agreements associated with the specified user ID.
     */
    @Override
    public List<Agreement> findByUser(Long userId, Pageable pageable) {
        return agreementRepository.findByUserId(userId, pageable);
    }

    /**
     * Retrieves an agreement by its ID.
     *
     * @param agreementId The ID of the agreement.
     * @return The agreement with the specified ID.
     * @throws IllegalArgumentException If no agreement is found with the specified ID.
     */
    @Override
    public Agreement findById(Long agreementId) {
        return agreementRepository.findById(agreementId)
            .orElseThrow(() -> new IllegalArgumentException("Not found agreement with id: " + agreementId));
    }

}