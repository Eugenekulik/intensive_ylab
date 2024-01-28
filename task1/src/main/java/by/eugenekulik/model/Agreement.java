package by.eugenekulik.model;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agreement {

    private Long id;
    private Long addressId;
    private Long userId;
}
