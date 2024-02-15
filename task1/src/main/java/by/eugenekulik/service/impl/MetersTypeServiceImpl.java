package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.MetersTypeMapper;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import by.eugenekulik.service.annotation.Valid;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Service class for handling operations related to meters types.
 * Manages the interaction with the underlying MetersTypeRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
public class MetersTypeServiceImpl implements MetersTypeService {

    private MetersTypeRepository metersTypeRepository;
    private MetersTypeMapper mapper;


    /**
     * Constructs a MetersTypeService with the specified MetersTypeRepository.
     *
     * @param metersTypeRepository The repository responsible for managing meters types.
     * @param mapper The mapper for MetersType model.
     */
    @Inject
    public MetersTypeServiceImpl(MetersTypeRepository metersTypeRepository, MetersTypeMapper mapper) {
        this.metersTypeRepository = metersTypeRepository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @Timed
    public MetersTypeDto create(@Valid({"id"}) MetersTypeDto metersTypeDto) {
        MetersType metersType = mapper.fromMetersTypeDto(metersTypeDto);
        return mapper.fromMetersType(metersTypeRepository.save(metersType));
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If no meters type is found with the specified name.
     */
    @Override
    @Timed
    public MetersTypeDto findByName(String name) {
        return metersTypeRepository.findByName(name)
            .map(mapper::fromMetersType)
            .orElseThrow(() -> new IllegalArgumentException("not found metersType with name: " + name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<MetersTypeDto> findAll() {
        return metersTypeRepository.findAll().stream()
            .map(mapper::fromMetersType)
            .toList();
    }
}
