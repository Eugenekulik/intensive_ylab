package by.eugenekulik.in.rest.filter;

import by.eugenekulik.security.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.io.IOException;


public class JwtFilter implements Filter {
  private static final String AUTHORIZATION = "Authorization";
  private final JwtProvider jwtProvider;

  private final UserDetailsService userDetailsService;

  public JwtFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
    this.jwtProvider = jwtProvider;
    this.userDetailsService = userDetailsService;
  }


  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    String token = getTokenFromRequest((HttpServletRequest) servletRequest);
    if (token != null && jwtProvider.validateToken(token)) {
      String userLogin = jwtProvider.getLoginFromToken(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private String getTokenFromRequest(HttpServletRequest servletRequest) {
    String bearer = servletRequest.getHeader(AUTHORIZATION);
    if(bearer!=null && bearer.startsWith("Bearer ")){
      return bearer.substring(7);
    }
    return null;
  }
}
