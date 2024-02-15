package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;

public record AgreementDto(
    @NotNull
    Long id,
    @NotNull
    Long userId,
    @NotNull
    Long addressId) {
    @Override
    public String toString() {
        return "AgreementDto{" +
            "id=" + id +
            ", userId=" + userId +
            ", addressId=" + addressId +
            '}';
    }
}
