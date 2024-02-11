package by.eugenekulik.service.logic.impl;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.Timed;
import by.eugenekulik.service.logic.MetersTypeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Service class for handling operations related to meters types.
 * Manages the interaction with the underlying MetersTypeRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
@Timed
public class MetersTypeServiceImpl implements MetersTypeService {

    private MetersTypeRepository metersTypeRepository;
    private TransactionManager transactionManager;

    /**
     * Constructs a MetersTypeService with the specified MetersTypeRepository.
     *
     * @param metersTypeRepository The repository responsible for managing meters types.
     * @param transactionManager   It is needed to wrap certain database interaction logic in a transaction.
     */
    @Inject
    public MetersTypeServiceImpl(MetersTypeRepository metersTypeRepository, TransactionManager transactionManager) {
        this.metersTypeRepository = metersTypeRepository;
        this.transactionManager = transactionManager;
    }

    /**
     * Creates a new meters type if a meters type with the same name doesn't already exist.
     *
     * @param metersType The meters type to be created.
     * @return The created meters type.
     * @throws IllegalArgumentException If a meters type with the same name already exists.
     */
    @Override
    public MetersType create(MetersType metersType) {
        return transactionManager.doInTransaction(() -> metersTypeRepository.save(metersType));
    }

    /**
     * Retrieves a meters type by its name.
     *
     * @param name The name of the meters type.
     * @return The meters type with the specified name.
     * @throws IllegalArgumentException If no meters type is found with the specified name.
     */
    @Override
    public MetersType findByName(String name) {
        return metersTypeRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("not found metersType with name: " + name));
    }

    /**
     * Retrieves a list of all meters types.
     *
     * @return A list of all meters types.
     */
    @Override
    public List<MetersType> findAll() {
        return metersTypeRepository.findAll();
    }
}
