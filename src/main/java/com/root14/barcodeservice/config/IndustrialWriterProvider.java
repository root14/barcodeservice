package com.root14.barcodeservice.config;

import com.google.zxing.oned.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * This class provides Spring Bean definitions for industrial 1D barcode writers.
 * It offers {@link Code39Writer}, {@link Code93Writer}, {@link Code128Writer},
 * {@link CodaBarWriter}, and {@link ITFWriter} from the Google ZXing library
 * as injectable components within a Spring application.
 * Each writer bean is initialized lazily, meaning it will only be created when it is first needed.
 */
@Configuration
public class IndustrialWriterProvider {
    /**
     * Provides an instance of {@link Code39Writer}.
     * This writer is used for generating Code 39 barcodes.
     *
     * @return A new instance of {@link Code39Writer}.
     */
    @Bean
    @Lazy
    public Code39Writer provideCode39Writer() {
        return new Code39Writer();
    }

    /**
     * Provides an instance of {@link Code93Writer}.
     * This writer is used for generating Code 93 barcodes.
     *
     * @return A new instance of {@link Code93Writer}.
     */
    @Bean
    @Lazy
    public Code93Writer provideCode93Writer() {
        return new Code93Writer();
    }

    /**
     * Provides an instance of {@link Code128Writer}.
     * This writer is used for generating Code 128 barcodes.
     *
     * @return A new instance of {@link Code128Writer}.
     */
    @Bean
    @Lazy
    public Code128Writer provideCode128Writer() {
        return new Code128Writer();
    }

    /**
     * Provides an instance of {@link CodaBarWriter}.
     * This writer is used for generating Codabar barcodes.
     *
     * @return A new instance of {@link CodaBarWriter}.
     */
    @Bean
    @Lazy
    public CodaBarWriter provideCodaBarWriter() {
        return new CodaBarWriter();
    }

    /**
     * Provides an instance of {@link ITFWriter}.
     * This writer is used for generating ITF (Interleaved 2 of 5) barcodes.
     *
     * @return A new instance of {@link ITFWriter}.
     */
    @Bean
    @Lazy
    public ITFWriter provideITFWriter() {
        return new ITFWriter();
    }
}