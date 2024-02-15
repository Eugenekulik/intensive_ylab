package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.service.AddressMapper;
import by.eugenekulik.service.annotation.Auditable;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import by.eugenekulik.service.AddressService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
    private AddressMapper addressMapper;

    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository The repository responsible for managing addresses.
     * @param addressMapper The mapper for address model.
     */
    @Inject
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }


    /**
     * Creates a new address if it doesn't already exist.
     *
     * @param addressDto The information of address to be created.
     * @return The created address.
     * @throws IllegalArgumentException If the address already exists.
     */
    @Override
    @Timed
    @Transactional
    public AddressDto create(@Valid AddressDto addressDto) {
        return addressMapper
            .fromAddress(addressRepository
                .save(addressMapper
                    .fromAddressDto(addressDto)));
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
    public List<AddressDto> getPage(Pageable pageable) {
        return addressRepository.getPage(pageable).stream()
            .map(addressMapper::fromAddress)
            .toList();
    }

    @Auditable
    @Override
    @Timed
    public AddressDto findById(long id) {
        return addressRepository.findById(id)
            .map(addressMapper::fromAddress)
            .orElseThrow(() -> new IllegalArgumentException("address with this id is not exists"));
    }

    @Override
    @Timed
    public List<AddressDto> findByUser(Long userId, Pageable pageable) {
        return addressRepository.findByUserId(userId, pageable)
            .stream()
            .map(addressMapper::fromAddress)
            .toList();
    }
}
