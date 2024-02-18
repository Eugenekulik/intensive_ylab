package by.eugenekulik.in.rest;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController handles HTTP requests related to users.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {
    private final UserService userService;

    /**
     * Retrieves a page of users.
     *
     * @param pageable The pageable configuration for retrieving the user page.
     * @return ResponseEntity containing a page of user DTOs.
     */
    @GetMapping
    @Auditable
    @RolesAllowed("ADMIN")
    public ResponseEntity<Iterable<UserDto>> getPage(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(userService.getPage(pageable));
    }
}
