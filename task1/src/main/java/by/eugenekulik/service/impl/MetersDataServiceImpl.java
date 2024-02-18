package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling operations related to meters data.
 * Manages the interaction with the underlying MetersDataRepository.
 *
 * @author Eugene Kulik
 */
@Service
@RequiredArgsConstructor
public class MetersDataServiceImpl implements MetersDataService {

    private final MetersDataRepository metersDataRepository;
    private final MetersTypeRepository metersTypeRepository;
    private final MetersDataMapper mapper;


    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If readings for the same meter type and agreement
     *                                  have already been submitted for the current month.
     */
    @Override
    @Transactional
    @Timed
    public MetersDataResponseDto create(MetersDataRequestDto metersDataRequestDto) {
        MetersData metersData = mapper.fromMetersDataDto(metersDataRequestDto);
        metersData.setPlacedAt(LocalDateTime.now());
        if (metersDataRepository.findByAgreementAndTypeAndMonth(metersData.getAgreementId(),
            metersData.getMetersTypeId(),
            metersData.getPlacedAt().toLocalDate()).isPresent())
            throw new IllegalArgumentException("The readings of this meter have " +
                "already been submitted this month");
        return mapper.fromMetersData(metersDataRepository.save(metersData));
    }


    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If no meters data is found.
     */
    @Override
    @Timed
    public MetersDataResponseDto findLastByAgreementAndType(Long agreementId, String typeName) {
        return metersTypeRepository.findByName(typeName)
            .flatMap(metersType -> metersDataRepository.findLastByAgreementAndType(agreementId, metersType.getId()))
            .map(mapper::fromMetersData)
            .orElseThrow(()->new IllegalArgumentException("not found metersData"));
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If not valid pageable.
     */
    @Override
    @Timed
    public List<MetersDataResponseDto> getPage(Pageable pageable) {
        return metersDataRepository.getPage(pageable).stream()
            .map(mapper::fromMetersData)
            .toList();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If pageable not valid.
     */
    @Override
    @Timed
    public List<MetersDataResponseDto> findByAgreementAndType(long agreementId, String typeName, Pageable pageable) {
        return metersTypeRepository.findByName(typeName)
            .map(metersType -> metersDataRepository
                .findByAgreementAndType(agreementId, metersType.getId(), pageable)
                .stream().map(mapper::fromMetersData).toList())
            .orElseThrow(()->new IllegalArgumentException("not found"));
    }
}
