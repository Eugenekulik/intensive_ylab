package by.eugenekulik.model;


import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * The {@code Agreement} class represents an agreement between a user and an address.
 * It is annotated with Lombok annotations for automatic generation of getters, setters,
 * constructors, and a toString method.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agreement {

    /**
     * The unique identifier for the agreement.
     */
    @NotNull
    private Long id;

    /**
     * The unique identifier for the associated address.
     */
    @NotNull
    private Long addressId;

    /**
     * The unique identifier for the user involved in the agreement.
     */
    @NotNull
    private Long userId;
}
