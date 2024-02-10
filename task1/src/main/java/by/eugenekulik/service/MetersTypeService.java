package by.eugenekulik.service;

import by.eugenekulik.model.MetersType;

import java.util.List;

public interface MetersTypeService {
    MetersType create(MetersType metersType);

    MetersType findByName(String name);

    List<MetersType> findAll();
}
