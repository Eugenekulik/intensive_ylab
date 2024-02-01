package by.eugenekulik.out.dao;

import by.eugenekulik.model.MetersType;

import java.util.List;
import java.util.Optional;

/**
 * The {@code MetersTypeRepository} interface defines methods for interacting with
 * meters type-related data in a repository.
 */
public interface MetersTypeRepository {

    /**
     * Retrieves meters type by its unique identifier.
     *
     * @param id The unique identifier of the meters type.
     * @return An {@code Optional} containing the meters type, or empty if not found.
     */
    Optional<MetersType> findById(Long id);

    /**
     * Retrieves meters type by its name.
     *
     * @param name The name of the meters type.
     * @return An {@code Optional} containing the meters type, or empty if not found.
     */
    Optional<MetersType> findByName(String name);

    /**
     * Saves meters type in the repository.
     *
     * @param metersType The meters type to save.
     * @return The saved meters type.
     */
    MetersType save(MetersType metersType);

    /**
     * Retrieves all meters types from the repository.
     *
     * @return A list of all meters types.
     */
    List<MetersType> findAll();
}
