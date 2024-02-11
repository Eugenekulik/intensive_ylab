package by.eugenekulik.service.mapper;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import org.mapstruct.Mapper;

@Mapper
public interface MetersTypeMapper {

    MetersType toMetersType(MetersTypeDto metersTypeDto);

    MetersTypeDto toMetersTypeDto(MetersType metersType);
}
