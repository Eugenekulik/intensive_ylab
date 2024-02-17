package by.eugenekulik.service.impl;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.service.UserMapper;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.annotation.Timed;
import jakarta.inject.Inject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class for handling operations related to users.
 * Manages the interaction with the underlying UserRepository.
 *
 * @author Eugene Kulik
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Inject
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }




    @Override
    public UserDto currentUser() {
        return userMapper.fromUser(userRepository
            .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElse(User.guest));
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
