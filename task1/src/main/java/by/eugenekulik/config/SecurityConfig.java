package by.eugenekulik.config;

import by.eugenekulik.in.rest.filter.JwtFilter;
import by.eugenekulik.out.dao.UserRepository;
import by.eugenekulik.out.dao.jdbc.repository.JdbcUserRepository;
import by.eugenekulik.security.JwtProvider;
import by.eugenekulik.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
@Import({JdbcUserRepository.class})
public class SecurityConfig {

    @Bean
    public MvcRequestMatcher.Builder matcher(HandlerMappingIntrospector introspector){
        MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);
        builder.servletPath("");
        return builder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter, MvcRequestMatcher.Builder builder) throws Exception {
        return http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(c->c
                    .requestMatchers(builder.pattern("/sign-in"), builder.pattern("/sign-up")).permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new JwtUserDetailsService(userRepository);
    }

    @Bean
    public JwtProvider jwtProvider(){
        return new JwtProvider();
    }

    @Bean JwtFilter jwtFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        return new JwtFilter(jwtProvider, userDetailsService);
    }

}
