package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.model.MetersData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetersDataMapper {

    @Mapping(target = "id", ignore = true)
    MetersData fromMetersDataDto(MetersDataRequestDto metersDataRequestDto);

    MetersDataResponseDto fromMetersData(MetersData metersData);
}
