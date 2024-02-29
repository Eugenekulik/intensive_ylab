package by.eugenekulik.dto;

import java.time.LocalDateTime;

public record MetersDataResponseDto (
    Long id,
    Long metersTypeId,
    Long agreementId,
    Double value,
    LocalDateTime placedAt
) {}
