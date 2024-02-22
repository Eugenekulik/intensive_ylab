package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MetersDataRequestDto(
    @NotNull
    Long agreementId,
    @NotNull
    Long metersTypeId,
    @NotNull
    @Positive
    Double value) {
    @Override
    public String toString() {
        return "MetersDataRequestDto{" +
            "agreementId=" + agreementId +
            ", metersTypeId=" + metersTypeId +
            ", value=" + value +
            '}';
    }
}
