package by.eugenekulik.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * The {@code MetersType} class represents a type of meters.
 * It is annotated with Lombok annotations for automatic generation of getters, setters, constructors, and a toString method.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MetersType {

    /**
     * The unique identifier for the meters type.
     */
    @NotNull
    private Long id;

    /**
     * The name of the meters type.
     */
    @NotNull
    @Pattern(regexp = "[a-z][a-z_]")
    private String name;
}
