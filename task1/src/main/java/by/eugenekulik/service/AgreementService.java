package by.eugenekulik.service;

import by.eugenekulik.dto.AgreementDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;

public interface AgreementService {
    AgreementDto create(AgreementDto agreementDto);

    List<AgreementDto> getPage(Pageable pageable);

    List<AgreementDto> findByUser(Long userId, Pageable pageable);

    AgreementDto findById(Long agreementId);

}
