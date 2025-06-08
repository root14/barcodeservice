package com.root14.barcodeservice;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.root14.barcodeservice.core.BarcodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BarcodeGeneratorTest {

    private BarcodeGenerator barcodeGenerator;

    @BeforeEach
    void setUp() {
        barcodeGenerator = new BarcodeGenerator()
                .setWriter(new MultiFormatWriter())
                .setBarcodeFormat(BarcodeFormat.QR_CODE);
    }

    @Test
    void generate_withBasicParams_shouldReturnImage() throws WriterException {
        BufferedImage image = barcodeGenerator.generate("hello QR", 200, 200);
        assertNotNull(image);
        assertEquals(200, image.getWidth());
        assertEquals(200, image.getHeight());
    }

    @Test
    void generate_withHints_shouldReturnImage() throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BufferedImage image = barcodeGenerator.generateQr("test with hints", 150, 150, hints);
        assertNotNull(image);
        assertEquals(150, image.getWidth());
        assertEquals(150, image.getHeight());
    }

    @Test
    void generate_withConfigAndHints_shouldReturnImage() throws WriterException {
        MatrixToImageConfig config = new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE);
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);

        BufferedImage image = barcodeGenerator.generateQr("with config", 120, 120, config, hints);
        assertNotNull(image);
    }

    @Test
    void generate_missingWriter_shouldThrowException() {
        BarcodeGenerator generator = new BarcodeGenerator().setBarcodeFormat(BarcodeFormat.QR_CODE);
        assertThrows(NullPointerException.class, () -> generator.generate("data", 100, 100));
    }

    @Test
    void generate_missingBarcodeFormat_shouldThrowException() {
        BarcodeGenerator generator = new BarcodeGenerator().setWriter(new MultiFormatWriter());
        assertThrows(Exception.class, () -> generator.generate("data", 100, 100));
    }
}
