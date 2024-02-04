package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.TransactionManager;

import java.util.List;

/**
 * Service class for handling operations related to addresses.
 * Manages the interaction with the underlying AddressRepository.
 *
 *
 * @author Eugene Kulik
 */
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final TransactionManager transactionManager;


    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository   The repository responsible for managing addresses.
     * @param transactionManager  It is needed to wrap certain database interaction logic in a transaction.
     */
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
        return transactionManager.doInTransaction(()->addressRepository.save(address));
    }


    /**
     * Retrieves a paginated list of addresses.
     *
     * @param page  The page number (starting from 0).
     * @param count The number of addresses to retrieve per page.
     * @return A list of addresses for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    @Override
    public List<Address> getPage(int page, int count) {
        return addressRepository.getPage(page, count);
    }
}
