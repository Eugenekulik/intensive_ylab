package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.service.MetersDataMapper;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import by.eugenekulik.service.annotation.Valid;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

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
public class MetersDataServiceImpl implements MetersDataService {

    private MetersDataRepository metersDataRepository;
    private MetersTypeRepository metersTypeRepository;
    private MetersDataMapper mapper;

    /**
     * Constructs a MetersDataService with the specified MetersDataRepository.
     *
     * @param metersDataRepository The repository responsible for managing meters data.
     * @param metersTypeRepository The repository responsible for managing meters type.
     * @param mapper               The mapper for model MetersData.
     */
    @Inject
    public MetersDataServiceImpl(MetersDataRepository metersDataRepository, MetersTypeRepository metersTypeRepository, MetersDataMapper mapper) {
        this.metersDataRepository = metersDataRepository;
        this.metersTypeRepository = metersTypeRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If readings for the same meter type and agreement
     *                                  have already been submitted for the current month.
     */
    @Override
    @Timed
    @Transactional
    public MetersDataDto create(@Valid({"id", "placedAt"}) MetersDataDto metersDataDto) {
        MetersData metersData = mapper.fromMetersDataDto(metersDataDto);
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
    public MetersDataDto findLastByAgreementAndType(Long agreementId, String typeName) {
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
    public List<MetersDataDto> getPage(Pageable pageable) {
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
    public List<MetersDataDto> findByAgreementAndType(long agreementId, String typeName, Pageable pageable) {
        return metersTypeRepository.findByName(typeName)
            .map(metersType -> metersDataRepository
                .findByAgreementAndType(agreementId, metersType.getId(), pageable)
                .stream().map(mapper::fromMetersData).toList())
            .orElseThrow(()->new IllegalArgumentException("not found"));
    }
}
