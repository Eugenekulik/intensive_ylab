package by.eugenekulik.security;

import by.eugenekulik.model.User;
import by.eugenekulik.out.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@Primary
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  /**
   * Loads user details by username.
   *
   * @param username The username for which to load details.
   * @return UserDetails containing user details.
   * @throws UsernameNotFoundException if the user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(JwtUserDetails::new)
        .orElseThrow(()-> new UsernameNotFoundException("user %s not found".formatted(username)));
  }

  /**
   * UserDetails implementation for JwtUserDetailsService.
   */
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
