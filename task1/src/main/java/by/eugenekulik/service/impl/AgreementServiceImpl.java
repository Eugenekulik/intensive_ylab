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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
