package by.eugenekulik.service.impl;

import by.eugenekulik.dto.AuthDto;
import by.eugenekulik.dto.RegistrationDto;
import by.eugenekulik.dto.UserDto;
import by.eugenekulik.exception.AuthenticationException;
import by.eugenekulik.exception.DatabaseInterectionException;
import by.eugenekulik.exception.RegistrationException;
import by.eugenekulik.model.Role;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.Pageable;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.security.Authentication;
import by.eugenekulik.service.UserMapper;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.annotation.Timed;
import by.eugenekulik.service.annotation.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Service class for handling operations related to users.
 * Manages the interaction with the underlying UserRepository.
 *
 * @author Eugene Kulik
 */
@ApplicationScoped
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private HttpSession session;

    @Inject
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, HttpSession httpSession) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.session = httpSession;
    }


    /**
     * {@inheritDoc}
     * @throws RegistrationException If a user with the same username or email already exists.
     */
    @Override
    @Timed
    @Transactional
    public UserDto register(@Valid RegistrationDto registrationDto) {
        try {
            User user = userMapper.fromRegistrationDto(registrationDto);
            user.setRole(Role.CLIENT);
            user = userRepository.save(user);
            session.setAttribute("authentication", new Authentication(user));
            return userMapper.fromUser(user);
        } catch (DatabaseInterectionException e) {
            throw new RegistrationException("user with this username or email already exists");
        }
    }

    /**
     * {@inheritDoc}
     * @throws AuthenticationException If the username or password is incorrect.
     */
    @Override
    @Timed
    public UserDto authorize(@Valid AuthDto authDto) {
        User user = userRepository.findByUsername(authDto.username())
            .orElseThrow(() -> new AuthenticationException(
                "The username or password you entered is incorrect"));
        if (!user.getPassword().equals(authDto.password())) {
            throw new AuthenticationException("The username or password you entered is incorrect");
        }
        session.setAttribute("authentication", new Authentication(user));
        return userMapper.fromUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Timed
    public List<UserDto> getPage(Pageable pageable) {
        return userRepository.getPage(pageable).stream()
            .map(userMapper::fromUser)
            .toList();
    }
}
