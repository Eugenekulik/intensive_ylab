package by.eugenekulik.model;

import lombok.*;


/**
 * The {@code User} class represents a user with various attributes.
 * It is annotated with Lombok annotations for automatic generation
 * of getters, setters, constructors, and a toString method.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * The unique identifier for the user.
     */
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The role of the user (ADMIN, CLIENT, GUEST).
     */
    private Role role;

    /**
     * Creates a guest user with the role set to GUEST.
     *
     * @return A guest user.
     */
    public static User guest() {
        return User.builder()
            .role(Role.GUEST)
            .build();
    }
}
