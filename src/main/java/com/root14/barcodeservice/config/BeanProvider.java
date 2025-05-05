package com.root14.barcodeservice.config;

import com.google.zxing.qrcode.QRCodeWriter;
import com.root14.barcodeservice.core.QrGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class BeanProvider {
    @Bean
    public QRCodeWriter provideQrCodeWriter() {
        return new QRCodeWriter();
    }

    @Bean
    @Lazy
    public QrGenerator provideQrGenerator(QRCodeWriter qrCodeWriter) {
        return new QrGenerator(qrCodeWriter);
    }
}
