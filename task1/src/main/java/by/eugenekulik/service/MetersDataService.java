package by.eugenekulik.service;

import by.eugenekulik.dto.MetersDataDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;

public interface MetersDataService {
    MetersDataDto create(MetersDataDto metersDataDto);


    MetersDataDto findLastByAgreementAndType(Long agreementId, String typeName);

    List<MetersDataDto> getPage(Pageable pageable);

    List<MetersDataDto> findByAgreementAndType(long agreementId, String typeName, Pageable pageable);
}
