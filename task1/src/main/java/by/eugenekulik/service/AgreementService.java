package by.eugenekulik.service;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.UserRepository;

import java.util.List;

/**
 * Service class for handling operations related to agreements.
 * Manages the interaction with the underlying AgreementRepository, AddressRepository, and UserRepository.
 * Provides methods for creating agreements, retrieving paginated lists of agreements,
 * finding agreements by user, and finding agreements by ID.
 *
 * @author Eugene Kulik
 */
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    /**
     * Constructs an AgreementService with the specified repositories.
     *
     * @param agreementRepository The repository responsible for managing agreements.
     * @param addressRepository   The repository responsible for managing addresses.
     * @param userRepository      The repository responsible for managing users.
     */
    public AgreementService(AgreementRepository agreementRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.agreementRepository = agreementRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
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
    public Agreement create(Agreement agreement) {
        if (addressRepository.findById(agreement.getAddressId()).isEmpty()) {
            throw new IllegalArgumentException("not found address with id: " + agreement.getAddressId());
        }
        if (userRepository.findById(agreement.getUserId()).isEmpty()) {
            throw new IllegalArgumentException("not found user with id: " + agreement.getUserId());
        }
        if (!agreementRepository
            .findByUserIdAndAddressId(agreement.getUserId(), agreement.getAddressId()).isEmpty()) {
            throw new IllegalArgumentException("agreement with this user and address are already exist");
        }
        return agreementRepository.save(agreement);
    }

    /**
     * Retrieves a paginated list of agreements.
     *
     * @param page  The page number (starting from 0).
     * @param count The number of agreements to retrieve per page.
     * @return A list of agreements for the specified page and count.
     * @throws IllegalArgumentException If page is negative or if count is less than 1.
     */
    public List<Agreement> getPage(int page, int count) {
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (count < 1) {
            throw new IllegalArgumentException("count must be positive");
        }
        return agreementRepository.getPage(page, count);
    }

    /**
     * Retrieves a list of agreements associated with the specified user ID.
     *
     * @param userId The ID of the user.
     * @return A list of agreements associated with the specified user ID.
     */
    public List<Agreement> findByUser(Long userId) {
        return agreementRepository.findByUserId(userId);
    }

    /**
     * Retrieves an agreement by its ID.
     *
     * @param agreementId The ID of the agreement.
     * @return The agreement with the specified ID.
     * @throws IllegalArgumentException If no agreement is found with the specified ID.
     */
    public Agreement findById(Long agreementId) {
        return agreementRepository.findById(agreementId)
            .orElseThrow(() -> new IllegalArgumentException("Not found agreement with id: " + agreementId));
    }
}
