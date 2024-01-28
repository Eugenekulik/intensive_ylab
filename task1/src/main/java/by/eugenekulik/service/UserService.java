package by.eugenekulik.service;

import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.in.console.Session;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;

import java.util.List;


public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User register(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent())
            throw new RegistrationException("A user with the same name already exists.");
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new RegistrationException("А user with this email is already registered.");
        user.setRole(Role.CLIENT);
        user = userRepository.save(user);
        Session.setCurrentUser(user);
        return user;
    }


    public User authorize(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow(
            ()->new AuthenticationException("The username or password you entered is incorrect"));
        if(!user.getPassword().equals(password))
            throw new AuthenticationException("The username or password you entered is incorrect");
        Session.setCurrentUser(user);
        return user;
    }


    public List<User> getPage(int page, int count){
        return userRepository.getPage(page, count);
    }




}
