package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MetersTypeDto(
    @NotNull
    Long id,
    @NotNull
    @Pattern(regexp = "[a-z][a-z_]")
    String name) {
    @Override
    public String toString() {
        return "MetersTypeDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
