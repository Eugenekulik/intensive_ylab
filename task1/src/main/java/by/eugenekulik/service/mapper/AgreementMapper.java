package by.eugenekulik.service.mapper;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.model.Agreement;
import org.mapstruct.Mapper;

@Mapper
public interface AgreementMapper {
    Agreement toAgreement(AgreementDto agreementDto);

    AgreementDto toAgreementDto(Agreement agreement);

}
