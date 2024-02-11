package by.eugenekulik.service.mapper;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.model.MetersData;
import org.mapstruct.Mapper;

@Mapper
public interface MetersDataMapper {
    MetersData toMetersData(MetersDataDto metersDataDto);

    MetersDataDto toMetersDataDto(MetersData metersData);
}
