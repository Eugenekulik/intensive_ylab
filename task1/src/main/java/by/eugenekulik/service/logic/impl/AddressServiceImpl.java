package by.eugenekulik.service.logic.impl;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;
import by.eugenekulik.service.aspect.Auditable;
import by.eugenekulik.service.aspect.Timed;
import by.eugenekulik.service.logic.AddressService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Service class for handling operations related to addresses.
 * Manages the interaction with the underlying AddressRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
@Timed
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    private TransactionManager transactionManager;


    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository  The repository responsible for managing addresses.
     * @param transactionManager It is needed to wrap certain database interaction logic in a transaction.
     */
    @Inject
    public AddressServiceImpl(AddressRepository addressRepository, TransactionManager transactionManager) {
        this.addressRepository = addressRepository;
        this.transactionManager = transactionManager;
    }


    /**
     * Creates a new address if it doesn't already exist.
     *
     * @param address The address to be created.
     * @return The created address.
     * @throws IllegalArgumentException If the address already exists.
     */
    @Override
    public Address create(Address address) {
        return transactionManager.doInTransaction(() -> addressRepository.save(address));
    }


    /**
     * Retrieves a paginated list of addresses.
     *
     * @param pageable class with information about page number and count number
     * @return A list of addresses for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    @Override
    public List<Address> getPage(Pageable pageable) {
        return addressRepository.getPage(pageable);
    }

    @Auditable
    @Override
    public Address findById(long id) {
        return addressRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("address with this id is not exists"));
    }

    @Override
    public List<Address> findByUser(Long userId, Pageable pageable) {
        return addressRepository.findByUserId(userId, pageable);
    }
}
