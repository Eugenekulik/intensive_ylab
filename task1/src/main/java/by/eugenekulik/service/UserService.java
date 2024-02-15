package by.eugenekulik.service;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;


public interface UserService {
    UserDto register(RegistrationDto registrationDto);

    UserDto authorize(AuthDto authDto);

    List<UserDto> getPage(Pageable pageable);
}
