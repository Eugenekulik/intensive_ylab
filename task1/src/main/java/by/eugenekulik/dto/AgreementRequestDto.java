package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AgreementRequestDto(
    @NotNull
    @Positive
    Long userId,
    @NotNull
    @Positive
    Long addressId) {}
