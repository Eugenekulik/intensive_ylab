package by.eugenekulik.service;

import by.eugenekulik.dto.MetersTypeDto;
import by.eugenekulik.model.MetersType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetersTypeMapper {

    MetersType fromMetersTypeDto(MetersTypeDto metersTypeDto);

    MetersTypeDto fromMetersType(MetersType metersType);
}
