package by.eugenekulik.model;

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
    private Long id;

    /**
     * The region where the address is located.
     */
    private String region;

    /**
     * The district within the region.
     */
    private String district;

    /**
     * The city of the address.
     */
    private String city;

    /**
     * The street of the address.
     */
    private String street;

    /**
     * The house number in the street.
     */
    private String houseNumber;

    /**
     * The apartment number within the house.
     */
    private String apartmentNumber;
}