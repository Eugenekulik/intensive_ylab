package by.eugenekulik.in.rest.filter;

import by.eugenekulik.security.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.io.IOException;

/**
 * JwtFilter is a Servlet filter responsible for handling JWT authentication in HTTP requests.
 */
@RequiredArgsConstructor
public class JwtFilter implements Filter {
  private static final String AUTHORIZATION = "Authorization";
  private final JwtProvider jwtProvider;
  private final UserDetailsService userDetailsService;

  /**
   * Filters incoming HTTP requests, validates JWT tokens, and sets up Spring Security authentication if the token is valid.
   *
   * @param servletRequest  The incoming ServletRequest.
   * @param servletResponse The outgoing ServletResponse.
   * @param filterChain     The FilterChain for processing the request.
   * @throws IOException      If an I/O error occurs during the filtering process.
   * @throws ServletException If a servlet-related error occurs during the filtering process.
   */
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

  /**
   * Retrieves the JWT token from the Authorization header in the HTTP request.
   *
   * @param servletRequest The HttpServletRequest from which to extract the token.
   * @return The JWT token or null if not found.
   */
  private String getTokenFromRequest(HttpServletRequest servletRequest) {
    String bearer = servletRequest.getHeader(AUTHORIZATION);
    if(bearer!=null && bearer.startsWith("Bearer ")){
      return bearer.substring(7);
    }
    return null;
  }
}
