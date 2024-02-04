package by.eugenekulik.service;

import by.eugenekulik.model.MetersData;

import java.time.LocalDate;
import java.util.List;

public interface MetersDataService {
    MetersData create(MetersData metersData);

    MetersData findByAgreementAndTypeAndMonth(Long agreementId, Long metersTypeId, LocalDate localDate);

    MetersData findLastByAgreementAndType(Long agreementId, Long metersTypeId);

    List<MetersData> getPage(int page, int count);
}
