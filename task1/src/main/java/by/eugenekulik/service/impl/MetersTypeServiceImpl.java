package by.eugenekulik.service.impl;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.service.MetersTypeMapper;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.starter.logging.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling operations related to meters types.
 * Manages the interaction with the underlying MetersTypeRepository.
 *
 * @author Eugene Kulik
 */
@Service
@RequiredArgsConstructor
public class MetersTypeServiceImpl implements MetersTypeService {

    private final MetersTypeRepository metersTypeRepository;
    private final MetersTypeMapper mapper;



    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public MetersTypeResponseDto create(MetersTypeRequestDto metersTypeRequestDto) {
        MetersType metersType = mapper.fromMetersTypeDto(metersTypeRequestDto);
        return mapper.fromMetersType(metersTypeRepository.save(metersType));
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException If no meters type is found with the specified name.
     */
    @Override
    @Timed
    public MetersTypeResponseDto findByName(String name) {
        return metersTypeRepository.findByName(name)
            .map(mapper::fromMetersType)
            .orElseThrow(() -> new IllegalArgumentException("not found metersType with name: " + name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<MetersTypeResponseDto> findAll() {
        return metersTypeRepository.findAll().stream()
            .map(mapper::fromMetersType)
            .toList();
    }
}
