package by.eugenekulik.model;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Agreement {

    private Long id;
    private Address address;
    private User owner;
}
