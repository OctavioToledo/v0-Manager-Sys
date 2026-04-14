package com.demoV1Project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register JSR310 module for Java 8 Date/Time types
        objectMapper.registerModule(new JavaTimeModule());
        
        // ISO-8601 formatting: "2026-04-08T17:00:00Z"
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Ensure UTC is used for serialization if not specified
        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        return objectMapper;
    }
}
