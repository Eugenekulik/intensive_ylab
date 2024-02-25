package by.eugenekulik.integration;

import by.eugenekulik.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class IntegrationTestConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public TestRestTemplate testRestTemplate(){
        return new TestRestTemplate(new RestTemplateBuilder()
        .rootUri("http://localhost:" + port));
    }

    @Bean
    public HeaderUtils headerUtils(JwtProvider jwtProvider){
        return new HeaderUtils(jwtProvider);
    }
}
