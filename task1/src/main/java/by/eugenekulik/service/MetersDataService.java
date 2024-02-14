package by.eugenekulik.service;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.MetersData;

import java.time.LocalDate;
import java.util.List;

public interface MetersDataService {
    MetersData create(MetersData metersData);

    MetersData findByAgreementAndTypeAndMonth(Long agreementId, Long metersTypeId, LocalDate localDate);

    MetersData findLastByAgreementAndType(Long agreementId, Long metersTypeId);

    List<MetersData> getPage(Pageable pageable);

    List<MetersData> findByAgreementAndType(long agreementId, Long id, Pageable pageable);
}
