package com.nqma.disbot.initconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        System.out.println("RestTemplateConfig.restTemplate");
        return new RestTemplate();
    }
}
