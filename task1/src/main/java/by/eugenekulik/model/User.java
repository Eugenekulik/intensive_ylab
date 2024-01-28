package by.eugenekulik.model;

import lombok.*;



@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;


    public static User guest(){
        return User.builder()
            .role(Role.GUEST)
            .build();
    }
}
