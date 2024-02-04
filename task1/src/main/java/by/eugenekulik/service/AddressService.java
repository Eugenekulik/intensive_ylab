package by.eugenekulik.service;

import by.eugenekulik.model.Address;

import java.util.List;

public interface AddressService {
    Address create(Address address);

    List<Address> getPage(int page, int count);
}
