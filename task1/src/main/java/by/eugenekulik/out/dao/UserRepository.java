package by.eugenekulik.out.dao;

import by.eugenekulik.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    List<User> getPage(int page, int count);
}
