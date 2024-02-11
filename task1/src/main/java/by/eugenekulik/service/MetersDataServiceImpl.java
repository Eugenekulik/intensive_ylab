package by.eugenekulik.service;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling operations related to meters data.
 * Manages the interaction with the underlying MetersDataRepository.
 *
 * @author Eugene Kulik
 */
public class MetersDataServiceImpl implements MetersDataService {

    private final MetersDataRepository metersDataRepository;
    private final TransactionManager transactionManager;

    /**
     * Constructs a MetersDataService with the specified MetersDataRepository.
     *
     * @param metersDataRepository The repository responsible for managing meters data.
     * @param transactionManager  It is needed to wrap certain database interaction logic in a transaction.
     */
    public MetersDataServiceImpl(MetersDataRepository metersDataRepository, TransactionManager transactionManager) {
        this.metersDataRepository = metersDataRepository;
        this.transactionManager = transactionManager;
    }

    /**
     * Creates meters data, sets the placement timestamp, and checks if readings
     * for the same meter type and agreement have already been submitted for the
     * current month.
     *
     * @param metersData The meters data to be created.
     * @return The created meters data.
     * @throws IllegalArgumentException If readings for the same meter type and agreement
     *                                  have already been submitted for the current month.
     */
    @Override
    public MetersData create(MetersData metersData) {
        metersData.setPlacedAt(LocalDateTime.now());
        if (metersDataRepository.findByAgreementAndTypeAndMonth(metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()).isPresent())
            throw new IllegalArgumentException("The readings of this meter have " +
                "already been submitted this month");
        return transactionManager.doInTransaction(()->metersDataRepository.save(metersData));
    }

    /**
     * Retrieves meters data by agreement, type, and month.
     *
     * @param agreementId  The ID of the agreement.
     * @param metersTypeId The ID of the meter type.
     * @param localDate    The timestamp representing the month.
     * @return The meters data for the specified agreement, type, and month.
     * @throws IllegalArgumentException If no meters data is found.
     */
    @Override
    public MetersData findByAgreementAndTypeAndMonth(Long agreementId, Long metersTypeId, LocalDate localDate) {
        return metersDataRepository.findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate)
            .orElseThrow(() -> new IllegalArgumentException("not fount metersData"));
    }

    /**
     * Retrieves the last meters data by agreement and type.
     *
     * @param agreementId  The ID of the agreement.
     * @param metersTypeId The ID of the meter type.
     * @return The last meters data for the specified agreement and type.
     * @throws IllegalArgumentException If no meters data is found.
     */
    @Override
    public MetersData findLastByAgreementAndType(Long agreementId, Long metersTypeId) {
        return metersDataRepository.findLastByAgreementAndType(agreementId, metersTypeId)
            .orElseThrow(() -> new IllegalArgumentException("not found metersData"));
    }

    /**
     * Retrieves a paginated list of meters data.
     *
     * @param page  The page number (starting from 0).
     * @param count The number of meters data to retrieve per page.
     * @return A list of meters data for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    @Override
    public List<MetersData> getPage(int page, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must be positive");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        return metersDataRepository.getPage(page, count);
    }
}