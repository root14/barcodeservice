package com.root14.barcodeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.ByteArrayOutputStream;

@Configuration
public class BeanProvider {

    @Bean
    @Lazy
    public ByteArrayOutputStream provideByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }
}
