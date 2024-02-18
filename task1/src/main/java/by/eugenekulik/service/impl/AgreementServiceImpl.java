package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.service.AgreementMapper;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for handling operations related to agreements.
 * Manages the interaction with the underlying AgreementRepository, AddressRepository, and UserRepository.
 *
 * @author Eugene Kulik
 */
@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    @Transactional
    public AgreementResponseDto create(AgreementRequestDto agreementRequestDto) {
        return agreementMapper
            .fromAgreement(agreementRepository
                .save(agreementMapper
                    .fromAgreementDto(agreementRequestDto)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<AgreementResponseDto> getPage(Pageable pageable) {
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
    public List<AgreementResponseDto> findByUser(Long userId, Pageable pageable) {
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
    public AgreementResponseDto findById(Long agreementId) {
        return agreementRepository.findById(agreementId)
            .map(agreementMapper::fromAgreement)
            .orElseThrow(() -> new IllegalArgumentException("Not found agreement with id: " + agreementId));
    }

}
