package by.eugenekulik.model;

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
    private Long id;


    /**
     * Contract number to which the meters data relate.
     */
    private Long agreementId;

    /**
     * This field stores information about the type of readings, whether warm,
     * cold water, heating, and so on.
     */
    private Long metersTypeId;

    /**
     * Meter value.
     */
    private Double value;
    private LocalDateTime placedAt;

}
