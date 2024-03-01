package by.eugenekulik.starter.audit.config;


import by.eugenekulik.starter.audit.advice.AuditAspect;
import by.eugenekulik.starter.audit.service.DefaultMessageBuilder;
import by.eugenekulik.starter.audit.service.DefaultRecordStorage;
import by.eugenekulik.starter.audit.service.MessageBuilder;
import by.eugenekulik.starter.audit.service.RecordStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AuditConfig {


    @Bean
    public AuditAspect auditAspect(MessageBuilder messageBuilder, RecordStorage recordStorage){
        return new AuditAspect(messageBuilder, recordStorage);
    }


    @Bean
    @ConditionalOnMissingBean(RecordStorage.class)
    public RecordStorage recordStorage(){
        return new DefaultRecordStorage();
    }

    @Bean
    @ConditionalOnMissingBean(MessageBuilder.class)
    public MessageBuilder messageBuilder(){
        return new DefaultMessageBuilder();
    }


}
