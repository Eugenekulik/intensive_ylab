package by.eugenekulik.service;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.model.MetersType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetersTypeMapper {

    @Mapping(target = "id", ignore = true)
    MetersType fromMetersTypeDto(MetersTypeRequestDto metersTypeRequestDto);

    MetersTypeResponseDto fromMetersType(MetersType metersType);
}
