package com.root14.barcodeservice.config;

import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class D2WriterProvider {

    @Bean
    @Lazy
    public QRCodeWriter provideQRCodeWriter() {
        return new QRCodeWriter();
    }

    @Bean
    @Lazy
    public DataMatrixWriter provideDataMatrixWriter() {
        return new DataMatrixWriter();
    }

    @Bean
    @Lazy
    public AztecWriter provideAztecWriter() {
        return new AztecWriter();
    }

    @Bean
    @Lazy
    public PDF417Writer providePDF417Writer() {
        return new PDF417Writer();
    }
}
