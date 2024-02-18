package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddressRequestDto(
    @NotNull
    @Pattern(regexp = "[A-Za-z][A-Za-z_\\- ]+")
    String region,
    @NotNull
    @Pattern(regexp = "[A-Za-z][A-Za-z_\\- ]+")
    String district,
    @NotNull
    @Pattern(regexp = "[A-Za-z][A-Za-z_\\- ]+")
    String city,
    @NotNull
    @Pattern(regexp = "[A-Za-z][A-Za-z_\\- ]+")
    String street,
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    String house,
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    String apartment) {

    @Override
    public String toString() {
        return "AddressRequestDto{" +
            "region='" + region + '\'' +
            ", district='" + district + '\'' +
            ", city='" + city + '\'' +
            ", street='" + street + '\'' +
            ", house='" + house + '\'' +
            ", apartment='" + apartment + '\'' +
            '}';
    }
}
