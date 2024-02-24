package by.eugenekulik.starter.logging.config;

import by.eugenekulik.starter.logging.advice.LoggingAspect;
import by.eugenekulik.starter.logging.advice.TimingAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class LoggingConfiguration {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public TimingAspect timingAspect(){
        return new TimingAspect();
    }


}
