package by.eugenekulik.service;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.model.Agreement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    Agreement fromAgreementDto(AgreementRequestDto agreementRequestDto);

    AgreementResponseDto fromAgreement(Agreement agreement);

}
