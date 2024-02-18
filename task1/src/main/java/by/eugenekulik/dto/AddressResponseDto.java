package by.eugenekulik.dto;

public record AddressResponseDto(
    Long id,
    String region,
    String district,
    String city,
    String street,
    String house,
    String apartment
) {
    @Override
    public String toString() {
        return "AddressResponseDto{" +
            "region='" + region + '\'' +
            ", district='" + district + '\'' +
            ", city='" + city + '\'' +
            ", street='" + street + '\'' +
            ", house='" + house + '\'' +
            ", apartment='" + apartment + '\'' +
            '}';
    }
}
