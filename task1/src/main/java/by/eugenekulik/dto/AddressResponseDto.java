package by.eugenekulik.dto;

public record AddressResponseDto(
    Long id,
    String region,
    String district,
    String city,
    String street,
    String house,
    String apartment
) {}
