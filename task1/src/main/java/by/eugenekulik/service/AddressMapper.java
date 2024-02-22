package by.eugenekulik.service;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address fromAddressDto(AddressRequestDto addressRequestDto);

    AddressResponseDto fromAddress(Address address);
}
