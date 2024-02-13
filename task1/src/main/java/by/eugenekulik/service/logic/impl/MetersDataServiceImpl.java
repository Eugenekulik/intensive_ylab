package by.eugenekulik.service.logic.impl;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.Timed;
import by.eugenekulik.service.aspect.Transactional;
import by.eugenekulik.service.logic.MetersDataService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling operations related to meters data.
 * Manages the interaction with the underlying MetersDataRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
@Timed
public class MetersDataServiceImpl implements MetersDataService {

    private MetersDataRepository metersDataRepository;

    /**
     * Constructs a MetersDataService with the specified MetersDataRepository.
     *
     * @param metersDataRepository The repository responsible for managing meters data.
     */
    @Inject
    public MetersDataServiceImpl(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
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
    @Transactional
    public MetersData create(MetersData metersData) {
        metersData.setPlacedAt(LocalDateTime.now());
        if (metersDataRepository.findByAgreementAndTypeAndMonth(metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()).isPresent())
            throw new IllegalArgumentException("The readings of this meter have " +
                "already been submitted this month");
        return metersDataRepository.save(metersData);
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
     * @param pageable class with information about page number and count number
     * @return A list of meters data for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    @Override
    public List<MetersData> getPage(Pageable pageable) {
        return metersDataRepository.getPage(pageable);
    }

    @Override
    public List<MetersData> findByAgreementAndType(long agreementId, Long meterTypeId, Pageable pageable) {
        return metersDataRepository.findByAgreementAndType(agreementId, meterTypeId, pageable);
    }
}
