package by.eugenekulik.service;

import by.eugenekulik.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * The UserService interface defines methods for managing users.
 * It provides functionality for user registration, authorization, and pagination.
 */
public interface UserService {


    UserDto currentUser();
    /**
     * Retrieves a paginated list of UserDto objects.
     *
     * @param pageable The Pageable object specifying the number of results and offset.
     * @return A List of UserDto objects for the specified page.
     */
    List<UserDto> getPage(Pageable pageable);

}

