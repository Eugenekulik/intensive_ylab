package by.eugenekulik.out.dao;

import by.eugenekulik.model.MetersType;

import java.util.List;
import java.util.Optional;

public interface MetersTypeRepository {

    Optional<MetersType> findById(Long id);
    Optional<MetersType> findByName(String name);

    MetersType save(MetersType metersType);
    List<MetersType> findAll();
}
