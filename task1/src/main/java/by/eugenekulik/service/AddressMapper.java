package by.eugenekulik.service;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address fromAddressDto(AddressDto addressDto);

    AddressDto fromAddress(Address address);
}
