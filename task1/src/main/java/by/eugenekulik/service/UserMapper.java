package by.eugenekulik.service;

import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User fromUserDto(UserDto userDto);


    UserDto fromUser(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User fromRegistrationDto(RegistrationDto registrationDto);
}
