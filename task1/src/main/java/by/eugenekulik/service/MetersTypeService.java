package by.eugenekulik.service;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;

import java.util.List;

/**
 * The MetersTypeService interface defines methods for managing meters types.
 * It provides functionality to create, retrieve, and list meters types.
 */
public interface MetersTypeService {

    /**
     * Creates a new meters type based on the provided MetersTypeRequestDto.
     *
     * @param metersTypeRequestDto The MetersTypeRequestDto containing information for creating the meters type.
     * @return The created MetersTypeResponseDto.
     */
    MetersTypeResponseDto create(MetersTypeRequestDto metersTypeRequestDto);

    /**
     * Retrieves the details of a meters type by its name.
     *
     * @param name The name of the meters type.
     * @return The MetersTypeResponseDto object for the specified name.
     */
    MetersTypeResponseDto findByName(String name);

    /**
     * Retrieves a list of all available meters types.
     *
     * @return A List of MetersTypeResponseDto objects representing all meters types.
     */
    List<MetersTypeResponseDto> findAll();
}

