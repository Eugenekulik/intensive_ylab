package by.eugenekulik.in.rest;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.service.AuthenticationService;
import by.eugenekulik.service.annotation.Auditable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/sign-up" , produces = "application/json")
    @Auditable
    public ResponseEntity<JwtResponseDto> singUp(@RequestBody RegistrationDto registrationDto){
        return new ResponseEntity<>(authenticationService.signUp(registrationDto), HttpStatus.CREATED);
    }


    @PostMapping(value = "/sign-in", produces = "application/json")
    @Auditable
    public ResponseEntity<JwtResponseDto> singUp(@RequestBody AuthDto authDto){
        return ResponseEntity.ok(authenticationService.signIn(authDto));
    }

}
