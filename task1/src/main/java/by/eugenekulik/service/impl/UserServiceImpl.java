package by.eugenekulik.service.impl;

import by.eugenekulik.dto.UserDto;
import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.service.UserMapper;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.annotation.Timed;
import lombok.RequiredArgsConstructor;
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
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
