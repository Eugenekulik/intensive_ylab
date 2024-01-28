package by.eugenekulik.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Address {

    private Long id;
    private String region;
    private String district;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;

}
