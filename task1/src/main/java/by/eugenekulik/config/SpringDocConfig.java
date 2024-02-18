package by.eugenekulik.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
            .title("Ylab")
        );
    }
}
