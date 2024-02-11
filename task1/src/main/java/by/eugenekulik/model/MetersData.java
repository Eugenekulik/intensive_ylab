package by.eugenekulik.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;


/**
 * This class is a data class that stores information about meter readings
 * for a specific contract for a specific month.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetersData {

    /**
     * Unique identifier of MetersData.
     */
    @NotNull
    private Long id;


    /**
     * Contract number to which the meters data relate.
     */
    @NotNull
    private Long agreementId;

    /**
     * This field stores information about the type of readings, whether warm,
     * cold water, heating, and so on.
     */
    @NotNull
    private Long metersTypeId;

    /**
     * Meter value.
     */
    @Positive
    private Double value;

    /**
     * Time when meters data was submitted.
     */
    @NotNull
    private LocalDateTime placedAt;

}
