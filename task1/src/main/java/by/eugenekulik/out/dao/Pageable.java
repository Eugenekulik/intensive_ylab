package by.eugenekulik.out.dao;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Pageable {

    private int page;
    private int count;


}
