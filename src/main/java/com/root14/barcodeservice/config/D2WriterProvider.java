package com.root14.barcodeservice.config;

import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * This class provides Spring Bean definitions for 2D barcode writers.
 * It offers {@link QRCodeWriter}, {@link DataMatrixWriter}, {@link AztecWriter}, and {@link PDF417Writer}
 * from the Google ZXing library as injectable components within a Spring application.
 * Each writer bean is initialized lazily, meaning it will only be created when it is first needed.
 */
@Configuration
public class D2WriterProvider {

    /**
     * Provides an instance of {@link QRCodeWriter}.
     * This writer is used for generating QR codes.
     *
     * @return A new instance of {@link QRCodeWriter}.
     */
    @Bean
    @Lazy
    public QRCodeWriter provideQRCodeWriter() {
        return new QRCodeWriter();
    }

    /**
     * Provides an instance of {@link DataMatrixWriter}.
     * This writer is used for generating Data Matrix codes.
     *
     * @return A new instance of {@link DataMatrixWriter}.
     */
    @Bean
    @Lazy
    public DataMatrixWriter provideDataMatrixWriter() {
        return new DataMatrixWriter();
    }

    /**
     * Provides an instance of {@link AztecWriter}.
     * This writer is used for generating Aztec codes.
     *
     * @return A new instance of {@link AztecWriter}.
     */
    @Bean
    @Lazy
    public AztecWriter provideAztecWriter() {
        return new AztecWriter();
    }

    /**
     * Provides an instance of {@link PDF417Writer}.
     * This writer is used for generating PDF417 codes.
     *
     * @return A new instance of {@link PDF417Writer}.
     */
    @Bean
    @Lazy
    public PDF417Writer providePDF417Writer() {
        return new PDF417Writer();
    }
}