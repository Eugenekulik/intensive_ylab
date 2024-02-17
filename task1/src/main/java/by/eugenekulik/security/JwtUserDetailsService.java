package by.eugenekulik.security;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Primary
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  public JwtUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      return new JwtUserDetails(user.get());
    }
    throw new UsernameNotFoundException(
        "User '" + username + "' not found");
  }


  private record JwtUserDetails(User user) implements UserDetails {
    @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
      }

      @Override
      public String getPassword() {
        return user.getPassword();
      }

      @Override
      public String getUsername() {
        return user.getUsername();
      }

      @Override
      public boolean isAccountNonExpired() {
        return true;
      }

      @Override
      public boolean isAccountNonLocked() {
        return true;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return true;
      }

      @Override
      public boolean isEnabled() {
        return true;
      }
    }
}
