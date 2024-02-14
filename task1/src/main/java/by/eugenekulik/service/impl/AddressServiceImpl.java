package by.eugenekulik.service.impl;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import by.eugenekulik.service.AddressService;
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
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;


    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository The repository responsible for managing addresses.
     */
    @Inject
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    /**
     * Creates a new address if it doesn't already exist.
     *
     * @param address The address to be created.
     * @return The created address.
     * @throws IllegalArgumentException If the address already exists.
     */
    @Override
    @Timed
    @Transactional
    public Address create(Address address) {
        return addressRepository.save(address);
    }


    /**
     * Retrieves a paginated list of addresses.
     *
     * @param pageable class with information about page number and count number
     * @return A list of addresses for the specified page and count.
     * @throws IllegalArgumentException If count is less than 1 or if page is negative.
     */
    @Override
    @Timed
    public List<Address> getPage(Pageable pageable) {
        return addressRepository.getPage(pageable);
    }

    @Auditable
    @Override
    @Timed
    public Address findById(long id) {
        return addressRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("address with this id is not exists"));
    }

    @Override
    @Timed
    public List<Address> findByUser(Long userId, Pageable pageable) {
        return addressRepository.findByUserId(userId, pageable);
    }
}
