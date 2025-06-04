package com.root14.barcodeservice;

import com.google.zxing.*;
import com.root14.barcodeservice.core.BarcodeReader;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BarcodeReaderTest {

    BarcodeReader barcodeReader = new BarcodeReader();

    //todo
    @Test
    void testRead_validBarcode_returnsResult() throws Exception {
        //src/test/resources/valid_barcode.png
        try (InputStream inputStream = getClass().getResourceAsStream("/valid_barcode.png")) {
            assertNotNull(inputStream, "barcode cannot found.");

            Result result = barcodeReader.read(inputStream, new HashMap<>());

            assertNotNull(result);
            assertEquals("42", result.getText());
        }
    }

    @Test
    void testRead_invalidImage_throwsIllegalArgumentException() {
        byte[] invalidImageData = "not an image".getBytes();
        InputStream inputStream = new ByteArrayInputStream(invalidImageData);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                barcodeReader.read(inputStream, new HashMap<>()));

        assertEquals("invalid image.", exception.getMessage());
    }

    @Test
    void testRead_validImageButNoBarcode_throwsNotFoundException() throws IOException {
        BufferedImage whiteImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        InputStream inputStream;

        try (var byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(whiteImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }

        assertThrows(NotFoundException.class, () ->
                barcodeReader.read(inputStream, new HashMap<>()));
    }
}
