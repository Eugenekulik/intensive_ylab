package by.eugenekulik.model;


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
    private Long id;

    /**
     * The name of the meters type.
     */
    private String name;
}
