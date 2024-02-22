package by.eugenekulik.service;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.JwtResponseDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user authentication operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Signs up a new user and generates a JWT token.
     *
     * @param registrationDto The registration data for the new user.
     * @return JwtResponseDto containing the generated JWT token.
     */
    public JwtResponseDto signUp(RegistrationDto registrationDto) {
        User user = User.builder()
            .username(registrationDto.username())
            .email(registrationDto.email())
            .password(passwordEncoder.encode(registrationDto.password()))
            .role(Role.CLIENT)
            .build();
        userRepository.save(user);
        String jwt = jwtProvider.generateToken(user.getUsername());
        return new JwtResponseDto(jwt);
    }

    /**
     * Signs in a user and generates a JWT token.
     *
     * @param authDto The authentication data for the user.
     * @return JwtResponseDto containing the generated JWT token.
     * @throws AuthenticationException if the username or password is not valid.
     */
    public JwtResponseDto signIn(AuthDto authDto) {
        return userRepository
            .findByUsername(authDto.username())
            .filter(u -> passwordEncoder.matches(authDto.password(), u.getPassword()))
            .map(u -> jwtProvider.generateToken(u.getUsername()))
            .map(JwtResponseDto::new)
            .orElseThrow(()->new AuthenticationException("not valid username or password") {
            });
    }
}
