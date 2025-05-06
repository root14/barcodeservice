package com.root14.barcodeservice.config;

import com.root14.barcodeservice.DynamicBeanManager;
import com.root14.barcodeservice.core.BarcodeGenerator;
import com.root14.barcodeservice.core.BarcodeReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CoreBeanProvider {

    @Bean
    @Lazy
    public BarcodeGenerator provideBarcodeGenerator() {
        return new BarcodeGenerator();
    }

    @Bean
    @Lazy
    public BarcodeReader provideBarcodeReader() {
        return new BarcodeReader();
    }

    @Bean
    public DynamicBeanManager provideDynamicBeanManager() {
        return new DynamicBeanManager();
    }

}
