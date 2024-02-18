package by.eugenekulik.service;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address fromAddressDto(AddressRequestDto addressRequestDto);

    AddressResponseDto fromAddress(Address address);
}
