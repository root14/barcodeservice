package com.root14.barcodeservice.config;

import com.google.zxing.oned.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ProductWriterProvider {

    @Bean
    @Lazy
    public UPCAWriter provideUPCAWriter() {
        return new UPCAWriter();
    }

    @Bean
    @Lazy
    public UPCEWriter provideUPCEWriter() {
        return new UPCEWriter();
    }

    @Bean
    @Lazy
    public EAN8Writer provideEAN8Writer() {
        return new EAN8Writer();
    }

    @Bean
    @Lazy
    public EAN13Writer provideEAN13Writer() {
        return new EAN13Writer();
    }

    /*@Bean
    @Lazy
    public UPCEANWriter provideUPCEANWriter() {
        return new UPCEANWriter;
    }*/


}
