package com.root14.barcodeservice.config;

import com.google.zxing.oned.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * This class provides Spring Bean definitions for product barcode writers.
 * It offers {@link UPCAWriter}, {@link UPCEWriter}, {@link EAN8Writer}, and {@link EAN13Writer}
 * from the Google ZXing library as injectable components within a Spring application.
 * Each writer bean is initialized lazily, meaning it will only be created when it is first needed.
 */
@Configuration
public class ProductWriterProvider {

    /**
     * Provides an instance of {@link UPCAWriter}.
     * This writer is used for generating UPC-A barcodes.
     *
     * @return A new instance of {@link UPCAWriter}.
     */
    @Bean
    @Lazy
    public UPCAWriter provideUPCAWriter() {
        return new UPCAWriter();
    }

    /**
     * Provides an instance of {@link UPCEWriter}.
     * This writer is used for generating UPC-E barcodes.
     *
     * @return A new instance of {@link UPCEWriter}.
     */
    @Bean
    @Lazy
    public UPCEWriter provideUPCEWriter() {
        return new UPCEWriter();
    }

    /**
     * Provides an instance of {@link EAN8Writer}.
     * This writer is used for generating EAN-8 barcodes.
     *
     * @return A new instance of {@link EAN8Writer}.
     */
    @Bean
    @Lazy
    public EAN8Writer provideEAN8Writer() {
        return new EAN8Writer();
    }

    /**
     * Provides an instance of {@link EAN13Writer}.
     * This writer is used for generating EAN-13 barcodes.
     *
     * @return A new instance of {@link EAN13Writer}.
     */
    @Bean
    @Lazy
    public EAN13Writer provideEAN13Writer() {
        return new EAN13Writer();
    }

    /*
    /**
     * Provides an instance of {@link UPCEANWriter}.
     * This writer is used for generating UPC/EAN barcodes.
     * This method is currently commented out and not in use.
     *
     * @return A new instance of {@link UPCEANWriter}.
     *
    @Bean
    @Lazy
    public UPCEANWriter provideUPCEANWriter() {
        return new UPCEANWriter();
    }*/
}