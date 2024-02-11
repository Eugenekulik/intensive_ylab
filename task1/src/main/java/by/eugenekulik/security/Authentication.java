package by.eugenekulik.security;

import by.eugenekulik.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Authentication {
    private User user;
}
