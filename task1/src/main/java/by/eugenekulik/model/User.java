package by.eugenekulik.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;


/**
 * The {@code User} class represents a user with various attributes.
 * It is annotated with Lombok annotations for automatic generation
 * of getters, setters, constructors, and a toString method.
 */
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Creates a guest user with the role set to GUEST.
     *
     * @return A guest user.
     */
    public static final User guest = User.builder()
        .role(Role.GUEST)
        .build();
    /**
     * The unique identifier for the user.
     */
    @NotNull
    private Long id;
    /**
     * The username of the user.
     */
    @NotNull
    @Length(min = 4, max = 50)
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]+")
    private String username;
    /**
     * The password of the user.
     */
    @NotNull
    @Length(min = 8,max = 128)
    private String password;
    /**
     * The email address of the user.
     */
    @Email
    @NotNull
    private String email;
    /**
     * The role of the user (ADMIN, CLIENT, GUEST).
     */
    @NotNull
    private Role role;
}
