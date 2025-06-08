package com.root14.barcodeservice;

import com.google.zxing.WriterException;
import com.root14.barcodeservice.controller.BarcodeGeneratorController;
import com.root14.barcodeservice.dto.ImageObject;
import com.root14.barcodeservice.service.BarcodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BarcodeGeneratorControllerTest {

    @Mock
    private BarcodeService barcodeService;

    @InjectMocks
    private BarcodeGeneratorController barcodeGeneratorController;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void generateBarcode_success() throws IOException, WriterException {
        String type = "QR_CODE";
        String data = "testdata";
        int width = 200;
        int height = 200;
        boolean store = false;
        byte[] barcodeBytes = "test-barcode-image".getBytes();
        String uuid = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        ImageObject mockImageObject = new ImageObject(uuid, barcodeBytes, createdAt);

        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.of(mockImageObject));

        ResponseEntity<?> responseEntity = barcodeGeneratorController.generateBarcode(type, data, width, height, store);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(ImageObject.class, responseEntity.getBody().getClass());
        ImageObject returnedImageObject = (ImageObject) responseEntity.getBody();
        assertEquals(uuid, returnedImageObject.uuid());
        assertEquals(barcodeBytes, returnedImageObject.barcode());
        assertEquals(createdAt, returnedImageObject.createdAt());
    }

    @Test
    void generateBarcode_notFound() throws IOException, WriterException {
        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = barcodeGeneratorController.generateBarcode("QR_CODE", "test-data", 200, 200, false);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void generateBarcode_ioException() throws IOException, WriterException {
        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenThrow(new IOException("Test IO Exception"));

        // expect an IOException to be thrown by the controller
        assertThrows(IOException.class, () -> {
            barcodeGeneratorController.generateBarcode("QR_CODE", "test-data", 200, 200, false);
        });
    }

    @Test
    void generateBarcode_writerException() throws IOException, WriterException {
        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenThrow(new WriterException("Test Writer Exception"));

        // expect a WriterException to be thrown by the controller
        assertThrows(WriterException.class, () -> {
            barcodeGeneratorController.generateBarcode("QR_CODE", "test-data", 200, 200, false);
        });
    }

    @Test
    void getBarcode_success() {
        String uuid = UUID.randomUUID().toString();
        byte[] barcodeBytes = "retrieved-barcode-image".getBytes();
        Instant createdAt = Instant.now();
        ImageObject mockImageObject = new ImageObject(uuid, barcodeBytes, createdAt);

        when(barcodeService.findBarcode(anyString())).thenReturn(Optional.of(mockImageObject));

        ResponseEntity<?> responseEntity = barcodeGeneratorController.getBarcode(uuid);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.IMAGE_PNG, responseEntity.getHeaders().getContentType());
        assertEquals("inline; filename=" + uuid + ".png", responseEntity.getHeaders().getFirst("Content-Disposition"));
        assertNotNull(responseEntity.getBody());
        assertEquals(barcodeBytes, responseEntity.getBody());
    }

    @Test
    void getBarcode_notFound() {
        when(barcodeService.findBarcode(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = barcodeGeneratorController.getBarcode(UUID.randomUUID().toString());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}