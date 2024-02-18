package by.eugenekulik.in.rest;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.service.AuthenticationService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController handles authentication-related HTTP requests.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param registrationDto The DTO containing user registration information.
     * @return ResponseEntity containing the JWT response for successful registration.
     */
    @PostMapping(value = "/sign-up" , produces = "application/json")
    @Auditable
    public ResponseEntity<JwtResponseDto> singUp(@Valid @RequestBody RegistrationDto registrationDto){
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authenticationService.signUp(registrationDto));
    }

    /**
     * Authenticates a user.
     *
     * @param authDto The DTO containing user authentication information.
     * @return ResponseEntity containing the JWT response for successful authentication.
     */
    @PostMapping(value = "/sign-in", produces = "application/json")
    @Auditable
    public ResponseEntity<JwtResponseDto> singIn(@Valid @RequestBody AuthDto authDto){
        return ResponseEntity
            .ok(authenticationService.signIn(authDto));
    }
}
