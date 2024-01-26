package by.eugenekulik.out.dao;

import by.eugenekulik.model.Agreement;

import java.util.Optional;

public interface AgreementRepository {
    Optional<Agreement> findById(Long id);
}
