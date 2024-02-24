package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.service.AuthenticationService;
import by.eugenekulik.starter.audit.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
     * @return  JwtResponseDto containing jwt token.
     */
    @PostMapping(value = "/sign-up" , produces = "application/json")
    @Auditable
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponseDto singUp(@Valid @RequestBody RegistrationDto registrationDto){
        return authenticationService.signUp(registrationDto);
    }

    /**
     * Authenticates a user.
     *
     * @param authDto The DTO containing user authentication information.
     * @return JwtResponseDto containing jwt token.
     */
    @PostMapping(value = "/sign-in", produces = "application/json")
    @Auditable
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto singIn(@Valid @RequestBody AuthDto authDto){
        return authenticationService.signIn(authDto);
    }
}
