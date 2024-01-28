package by.eugenekulik.out.dao;

import by.eugenekulik.model.MetersData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MetersDataRepository {
    Optional<MetersData> findById(Long id);
    MetersData save(MetersData metersData);



    Optional<MetersData> findByAgreementAndTypeAndMonth(Long agreementId,
                                                        Long metersTypeId,
                                                        LocalDate placedAt);

    List<MetersData> getPage(int page, int count);

    List<MetersData> getPageByAgreement(Long agreementId, int page, int count);
    Optional<MetersData> findLastByAgreementAndType(Long agreementId, Long metersTypeId);

}
