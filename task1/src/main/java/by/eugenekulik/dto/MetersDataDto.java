package by.eugenekulik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record MetersDataDto(
    @NotNull
    Long id,
    @NotNull
    Long agreementId,
    @NotNull
    Long metersTypeId,
    @NotNull
    @Positive
    Double value,
    @NotNull
    LocalDateTime placedAt) {
    @Override
    public String toString() {
        return "MetersDataDto{" +
            "id=" + id +
            ", agreementId=" + agreementId +
            ", metersTypeId=" + metersTypeId +
            ", value=" + value +
            ", placedAt=" + placedAt +
            '}';
    }
}
