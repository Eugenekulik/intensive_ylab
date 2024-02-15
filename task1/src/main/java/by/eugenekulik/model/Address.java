package by.eugenekulik.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * The {@code Address} class represents an address with various components.
 * It is annotated with Lombok annotations for automatic generation of getters, setters,
 * constructors, and a toString method.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Address {

    /**
     * The unique identifier for the address.
     */
    @NotNull
    private Long id;

    /**
     * The region where the address is located.
     */
    @NotNull
    @Pattern(regexp = "[a-z_]+")
    private String region;

    /**
     * The district within the region.
     */
    @NotNull
    @Pattern(regexp = "[a-z_]+")
    private String district;

    /**
     * The city of the address.
     */
    @NotNull
    @Pattern(regexp = "[a-z_]+")
    private String city;

    /**
     * The street of the address.
     */
    @NotNull
    @Pattern(regexp = "[a-z0-9_]+")
    private String street;

    /**
     * The house number in the street.
     */
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    private String house;

    /**
     * The apartment number within the house.
     */
    @NotNull
    @Pattern(regexp = "[a-z0-9]+")
    private String apartment;
}