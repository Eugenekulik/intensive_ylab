package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;

public record AgreementRequestDto(
    @NotNull
    Long userId,
    @NotNull
    Long addressId) {
    @Override
    public String toString() {
        return "AgreementRequestDto{" +
            "userId=" + userId +
            ", addressId=" + addressId +
            '}';
    }
}
