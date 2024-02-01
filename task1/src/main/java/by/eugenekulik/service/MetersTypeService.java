package by.eugenekulik.service;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;

import java.util.List;

/**
 * Service class for handling operations related to meters types.
 * Manages the interaction with the underlying MetersTypeRepository.
 * Provides methods for creating meters types, finding meters types by name,
 * and retrieving a list of all meters types.
 *
 * @author Eugene Kulik
 */
public class MetersTypeService {

    private final MetersTypeRepository metersTypeRepository;

    /**
     * Constructs a MetersTypeService with the specified MetersTypeRepository.
     *
     * @param metersTypeRepository The repository responsible for managing meters types.
     */
    public MetersTypeService(MetersTypeRepository metersTypeRepository) {
        this.metersTypeRepository = metersTypeRepository;
    }

    /**
     * Creates a new meters type if a meters type with the same name doesn't already exist.
     *
     * @param metersType The meters type to be created.
     * @return The created meters type.
     * @throws IllegalArgumentException If a meters type with the same name already exists.
     */
    public MetersType create(MetersType metersType) {
        if (metersTypeRepository.findByName(metersType.getName()).isPresent()) {
            throw new IllegalArgumentException(
                "meters with name " + metersType.getName() + " are already exist");
        }
        return metersTypeRepository.save(metersType);
    }

    /**
     * Retrieves a meters type by its name.
     *
     * @param name The name of the meters type.
     * @return The meters type with the specified name.
     * @throws IllegalArgumentException If no meters type is found with the specified name.
     */
    public MetersType findByName(String name) {
        return metersTypeRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("not found metersType with name: " + name));
    }

    /**
     * Retrieves a list of all meters types.
     *
     * @return A list of all meters types.
     */
    public List<MetersType> findAll() {
        return metersTypeRepository.findAll();
    }
}
