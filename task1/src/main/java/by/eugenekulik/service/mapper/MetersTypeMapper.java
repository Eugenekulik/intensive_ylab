package by.eugenekulik.service.mapper;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import org.mapstruct.Mapper;

@Mapper
public interface MetersTypeMapper {

    MetersType fromMetersTypeDto(MetersTypeDto metersTypeDto);

    MetersTypeDto fromMetersType(MetersType metersType);
}
