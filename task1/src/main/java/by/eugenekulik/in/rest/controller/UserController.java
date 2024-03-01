package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.service.UserService;
import by.eugenekulik.starter.audit.annotation.Auditable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController handles HTTP requests related to users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {
    private final UserService userService;

    /**
     * Retrieves a page of users.
     *
     * @param pageable The pageable configuration for retrieving the user page.
     * @return Iterable of users.
     */
    @GetMapping
    @Auditable
    @Secured("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<UserDto> getPage(@PageableDefault Pageable pageable){
        return userService.getPage(pageable);
    }
}
