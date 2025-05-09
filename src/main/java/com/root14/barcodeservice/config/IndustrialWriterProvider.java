package com.root14.barcodeservice.config;

import com.google.zxing.oned.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class IndustrialWriterProvider {
    @Bean
    @Lazy
    public Code39Writer provideCode39Writer() {
        return new Code39Writer();
    }

    @Bean
    @Lazy
    public Code93Writer provideCode93Writer() {
        return new Code93Writer();
    }

    @Bean
    @Lazy
    public Code128Writer provideCode128Writer() {
        return new Code128Writer();
    }

    @Bean
    @Lazy
    public CodaBarWriter provideCodaBarWriter() {
        return new CodaBarWriter();
    }

    @Bean
    @Lazy
    public ITFWriter provideITFWriter() {
        return new ITFWriter();
    }
}
