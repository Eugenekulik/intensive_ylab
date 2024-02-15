package by.eugenekulik.service;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;


/**
 * The UserService interface defines methods for managing users.
 * It provides functionality for user registration, authorization, and pagination.
 */
public interface UserService {

    /**
     * Registers a new user based on the provided RegistrationDto.
     *
     * @param registrationDto The RegistrationDto containing information for user registration.
     * @return The created UserDto.
     */
    UserDto register(RegistrationDto registrationDto);

    /**
     * Authorizes a user based on the provided AuthDto.
     *
     * @param authDto The AuthDto containing authentication information.
     * @return The authorized UserDto.
     */
    UserDto authorize(AuthDto authDto);

    /**
     * Retrieves a paginated list of UserDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of UserDto objects for the specified page.
     */
    List<UserDto> getPage(Pageable pageable);
}

