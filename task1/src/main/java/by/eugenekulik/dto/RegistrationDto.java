package by.eugenekulik.dto;

import by.eugenekulik.model.Role;
import jakarta.validation.constraints.*;

public record RegistrationDto(
    @NotNull
    @Min(4)
    @Max(50)
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
    String username,
    @NotNull
    @Min(8)
    @Max(128)
    String password,
    @Email
    @NotNull
    String email,
    @NotNull
    Role role
) {
}
