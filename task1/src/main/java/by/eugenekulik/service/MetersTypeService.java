package by.eugenekulik.service;

import by.eugenekulik.dto.MetersTypeDto;
import java.util.List;

public interface MetersTypeService {
    MetersTypeDto create(MetersTypeDto metersTypeDto);

    MetersTypeDto findByName(String name);

    List<MetersTypeDto> findAll();
}
