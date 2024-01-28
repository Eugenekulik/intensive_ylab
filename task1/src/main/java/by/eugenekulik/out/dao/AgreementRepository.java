package by.eugenekulik.out.dao;

import by.eugenekulik.model.Agreement;

import java.util.List;
import java.util.Optional;

public interface AgreementRepository {
    Optional<Agreement> findById(Long id);

    Agreement save(Agreement agreement);

    List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId);

    List<Agreement> getPage(int page, int count);

    List<Agreement> findByUserId(Long id);
}
