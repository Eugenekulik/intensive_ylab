package by.eugenekulik.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtProvider {
  // The secret key for signing JWTs
  private SecretKey key = Jwts.SIG.HS256.key().build();

  /**
   * Generates a JWT token for the specified username.
   *
   * @param username The username for which the token is generated.
   * @return The generated JWT token.
   */
  public String generateToken(String username) {
    Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
        .claims(Map.of("username", username))
        .expiration(date)
        .signWith(key)
        .compact();
  }

  /**
   * Validates the provided JWT token.
   *
   * @param token The JWT token to validate.
   * @return True if the token is valid, false otherwise.
   */
  public boolean validateToken(String token){
    try{
      Jwts.parser().verifyWith(key).build().parse(token);
      return true;
    } catch (Exception e){
      log.error("token validating error");
    }
    return false;
  }

  /**
   * Extracts the username from the provided JWT token.
   *
   * @param token The JWT token from which to extract the username.
   * @return The username extracted from the token.
   */
  public String getLoginFromToken(String token){
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        .getPayload().get("username", String.class);
  }
}
