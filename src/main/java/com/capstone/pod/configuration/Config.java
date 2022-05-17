package com.capstone.pod.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration

public class Config {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
