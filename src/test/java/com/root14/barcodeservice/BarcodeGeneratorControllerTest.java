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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BarcodeGeneratorControllerTest {

    @Mock
    private BarcodeService barcodeService;

    @InjectMocks
    private BarcodeGeneratorController barcodeGeneratorController;

    private MockMvc mockMvc;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(barcodeGeneratorController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void generateBarcode_success() throws Exception {
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

        mockMvc.perform(get("/generate").param("type", type).param("data", data).param("width", String.valueOf(width)).param("height", String.valueOf(height)).param("store", String.valueOf(store)).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.uuid").value(uuid)).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.barcode").exists());
    }

    @Test
    void generateBarcode_notFound() throws Exception {
        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(Optional.empty());

        mockMvc.perform(get("/generate").param("type", "QR_CODE").param("data", "test-data").param("width", "200").param("height", "200").param("store", "false").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void generateBarcode_ioException() throws Exception {
        when(barcodeService.generate(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenThrow(new IOException("Test IO Exception"));

        assertThrows(IOException.class, () -> mockMvc.perform(get("/generate").param("type", "QR_CODE").param("data", "test-data").param("width", "200").param("height", "200").param("store", "false")).andReturn().getResponse().getContentAsString());
    }

    @Test
    void getBarcode_success() throws Exception {
        String uuid = UUID.randomUUID().toString();
        byte[] barcodeBytes = "retrieved-barcode-image".getBytes();
        Instant createdAt = Instant.now();
        ImageObject mockImageObject = new ImageObject(uuid, barcodeBytes, createdAt);

        when(barcodeService.findBarcode(anyString())).thenReturn(Optional.of(mockImageObject));

        mockMvc.perform(get("/getBarcode").param("uuid", uuid).accept(MediaType.IMAGE_PNG)).andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG.toString())).andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + uuid + ".png")).andExpect(content().bytes(barcodeBytes));
    }

    @Test
    void getBarcode_notFound() throws Exception {
        when(barcodeService.findBarcode(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/getBarcode").param("uuid", UUID.randomUUID().toString()).accept(MediaType.IMAGE_PNG)).andExpect(status().isNotFound());
    }

    @Test
    void generateBarcode_defaults() throws Exception {
        String type = "QR_CODE";
        String data = "testdata";
        byte[] barcodeBytes = "test-barcode-image-default".getBytes();
        String uuid = UUID.randomUUID().toString();
        Instant createdAt = Instant.now();

        ImageObject mockImageObject = new ImageObject(uuid, barcodeBytes, createdAt);

        when(barcodeService.generate(eq(type), eq(data), eq(400), eq(400), eq(false))).thenReturn(Optional.of(mockImageObject));

        mockMvc.perform(get("/generate").param("type", type).param("data", data).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.uuid").value(uuid));
    }
}