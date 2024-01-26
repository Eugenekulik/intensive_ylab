package by.eugenekulik.out.dao;

import by.eugenekulik.model.MetersData;

import java.util.Optional;

public interface MetersDataRepository {
    Optional<MetersData> findById(Long id);
}
