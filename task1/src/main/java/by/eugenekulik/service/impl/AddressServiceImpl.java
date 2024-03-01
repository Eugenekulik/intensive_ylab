package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.service.AddressMapper;
import by.eugenekulik.service.AddressService;
import by.eugenekulik.starter.logging.annotation.Timed;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for handling operations related to addresses.
 * Manages the interaction with the underlying AddressRepository.
 *
 * @author Eugene Kulik
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    /**
     * Constructs an AddressService with the specified AddressRepository.
     *
     * @param addressRepository The repository responsible for managing addresses.
     * @param addressMapper The mapper for address model.
     */
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @Timed
    public AddressResponseDto create(AddressRequestDto addressRequestDto) {
        return addressMapper
            .fromAddress(addressRepository
                .save(addressMapper
                    .fromAddressDto(addressRequestDto)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<AddressResponseDto> getPage(Pageable pageable) {
        return addressRepository.getPage(pageable).stream()
            .map(addressMapper::fromAddress)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public AddressResponseDto findById(long id) {
        return addressRepository.findById(id)
            .map(addressMapper::fromAddress)
            .orElseThrow(() -> new IllegalArgumentException("address with this id is not exists"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<AddressResponseDto> findByUser(Long userId, Pageable pageable) {
        return addressRepository.findByUserId(userId, pageable)
            .stream()
            .map(addressMapper::fromAddress)
            .toList();
    }
}
