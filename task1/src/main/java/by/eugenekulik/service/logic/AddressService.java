package by.eugenekulik.service.logic;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.Address;

import java.util.List;

public interface AddressService {
    Address create(Address address);

    List<Address> getPage(Pageable pageable);

    Address findById(long id);

    List<Address> findByUser(Long userId, Pageable pageable);
}
