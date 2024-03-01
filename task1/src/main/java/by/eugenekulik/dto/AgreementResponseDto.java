package by.eugenekulik.dto;

public record AgreementResponseDto(
    Long id,
    Long userId,
    Long addressId,
    Boolean active
){}
