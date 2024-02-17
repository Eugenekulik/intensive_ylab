package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.MetersData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetersDataMapper {
    MetersData fromMetersDataDto(MetersDataDto metersDataDto);

    MetersDataDto fromMetersData(MetersData metersData);
}
