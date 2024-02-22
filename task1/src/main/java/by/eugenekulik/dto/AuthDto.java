package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record AuthDto(
    @NotNull
    @Length(min = 4, max = 50)
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
    String username,
    @NotNull
    @Length(min = 8, max = 128)
    String password
) {
    @Override
    public String toString() {
        return "AuthDto{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
