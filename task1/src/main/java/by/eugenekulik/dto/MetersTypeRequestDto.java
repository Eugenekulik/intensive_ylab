package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MetersTypeRequestDto(
    @NotNull
    @Pattern(regexp = "[a-z][a-z_]+")
    String name) {
    @Override
    public String toString() {
        return "MetersTypeRequestDto{" +
            "name='" + name + '\'' +
            '}';
    }
}
