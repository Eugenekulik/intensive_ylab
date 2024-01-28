package by.eugenekulik.service;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;


public class AddressService {


    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    public Address create(Address address){
        if(addressRepository.isPresent(address))
            throw new IllegalArgumentException("This address already exists");
        return addressRepository.save(address);
    }





}
