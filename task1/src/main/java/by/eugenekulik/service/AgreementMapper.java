package by.eugenekulik.service;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.model.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(target = "id", ignore = true)
    Agreement fromAgreementDto(AgreementRequestDto agreementRequestDto);

    AgreementResponseDto fromAgreement(Agreement agreement);

}
