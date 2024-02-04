package by.eugenekulik.service;

import by.eugenekulik.model.Agreement;

import java.util.List;

public interface AgreementService {
    Agreement create(Agreement agreement);

    List<Agreement> getPage(int page, int count);

    List<Agreement> findByUser(Long userId);

    Agreement findById(Long agreementId);
}
