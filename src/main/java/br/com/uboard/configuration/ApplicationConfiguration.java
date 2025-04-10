package br.com.uboard.configuration;

import br.com.uboard.common.CustomObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    CustomObjectMapper customObjectMapper() {
        return new CustomObjectMapper();
    }
}
