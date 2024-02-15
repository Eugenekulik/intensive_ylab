package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddressDto(
    Long id,

    @NotNull
    @Pattern(regexp = "[a-z_]+")
    String region,
    @NotNull
    @Pattern(regexp = "[a-z_]+")
    String district,
    @NotNull
    @Pattern(regexp = "[a-z_]+")
    String city,
    @NotNull
    @Pattern(regexp = "[a-z0-9_]+")
    String street,
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    String house,
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    String apartment) {

    @Override
    public String toString() {
        return "AddressDto{" +
            "id=" + id +
            ", region='" + region + '\'' +
            ", district='" + district + '\'' +
            ", city='" + city + '\'' +
            ", street='" + street + '\'' +
            ", house='" + house + '\'' +
            ", apartment='" + apartment + '\'' +
            '}';
    }
}
