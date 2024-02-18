package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.model.MetersData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetersDataMapper {
    MetersData fromMetersDataDto(MetersDataRequestDto metersDataRequestDto);

    MetersDataResponseDto fromMetersData(MetersData metersData);
}
