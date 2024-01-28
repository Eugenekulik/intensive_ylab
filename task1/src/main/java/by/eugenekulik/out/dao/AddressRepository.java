package by.eugenekulik.out.dao;

import by.eugenekulik.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    Optional<Address> findById(Long id);

    boolean isPresent(Address address);

    Address save(Address address);

    List<Address> getPage(int page, int count);
}
