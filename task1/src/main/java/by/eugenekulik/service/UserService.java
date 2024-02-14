package by.eugenekulik.service;

import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.model.User;

import java.util.List;


public interface UserService {
    User register(User user);

    User authorize(String username, String password);

    List<User> getPage(Pageable pageable);
}
