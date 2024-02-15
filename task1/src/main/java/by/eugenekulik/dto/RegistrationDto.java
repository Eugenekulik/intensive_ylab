package by.eugenekulik.dto;

import by.eugenekulik.model.Role;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record RegistrationDto(
    @NotNull
    @Length(min = 4, max = 50)
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
    String username,
    @NotNull
    @Length(min = 8, max = 128)
    String password,
    @Email
    @NotNull
    String email
) {
}
