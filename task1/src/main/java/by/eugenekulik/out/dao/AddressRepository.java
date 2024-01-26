package by.eugenekulik.out.dao;

import by.eugenekulik.model.Address;

import java.util.Optional;

public interface AddressRepository {
    Optional<Address> findById(Long id);
}
