package by.eugenekulik.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtProvider {

  private SecretKey key = Jwts.SIG.HS256.key().build();


  public String generateToken(String username) {
    Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
        .claims(Map.of("username", username))
        .expiration(date)
        .signWith(key)
        .compact();
  }


  public boolean validateToken(String token){
    try{
      Jwts.parser().verifyWith(key).build().parse(token);
      return true;
    } catch (Exception e){
      log.error("token validating error");
    }
    return false;
  }

  public String getLoginFromToken(String token){
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
        .getPayload().get("username", String.class);
  }
}
