package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record MetersDataRequestDto(
    @NotNull
    @Positive
    Long agreementId,
    @NotNull
    @Positive
    Long metersTypeId,
    @NotNull
    @PositiveOrZero
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
