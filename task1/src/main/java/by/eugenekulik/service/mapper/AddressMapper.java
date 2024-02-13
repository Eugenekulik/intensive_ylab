package by.eugenekulik.service.mapper;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.model.Address;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {
    Address fromAddressDto(AddressDto addressDto);

    AddressDto fromAddress(Address address);
}
