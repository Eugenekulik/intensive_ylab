package by.eugenekulik.service;

import by.eugenekulik.dto.MetersTypeDto;
import java.util.List;

/**
 * The MetersTypeService interface defines methods for managing meters types.
 * It provides functionality to create, retrieve, and list meters types.
 */
public interface MetersTypeService {

    /**
     * Creates a new meters type based on the provided MetersTypeDto.
     *
     * @param metersTypeDto The MetersTypeDto containing information for creating the meters type.
     * @return The created MetersTypeDto.
     */
    MetersTypeDto create(MetersTypeDto metersTypeDto);

    /**
     * Retrieves the details of a meters type by its name.
     *
     * @param name The name of the meters type.
     * @return The MetersTypeDto object for the specified name.
     */
    MetersTypeDto findByName(String name);

    /**
     * Retrieves a list of all available meters types.
     *
     * @return A List of MetersTypeDto objects representing all meters types.
     */
    List<MetersTypeDto> findAll();
}

