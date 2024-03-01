package by.eugenekulik.e2e;

import by.eugenekulik.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public class HeaderUtils {
    @Autowired
    private final JwtProvider jwtProvider;

    public HttpHeaders withAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.generateToken("admin"));
        return headers;
    }

    public HttpHeaders withClientToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.generateToken("user1"));
        return headers;
    }

}
