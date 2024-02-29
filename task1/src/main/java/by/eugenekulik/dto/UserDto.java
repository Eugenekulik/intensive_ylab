package by.eugenekulik.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserDto(

    @NotNull
    Long id,
    @NotNull
    @Length(min = 4, max = 50)
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
    String username,
    @Email
    @NotNull
    String email,
    @NotNull
    String role) {}
