package by.eugenekulik.service;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Agreement;

import java.util.List;

public interface AgreementService {
    Agreement create(Agreement agreement);

    List<Agreement> getPage(Pageable pageable);

    List<Agreement> findByUser(Long userId, Pageable pageable);

    Agreement findById(Long agreementId);

}
