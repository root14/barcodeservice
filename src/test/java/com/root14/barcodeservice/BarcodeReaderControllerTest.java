package com.root14.barcodeservice;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.root14.barcodeservice.controller.BarcodeReaderController;
import com.root14.barcodeservice.service.BarcodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarcodeReaderController.class)
class BarcodeReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BarcodeService barcodeService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BarcodeService barcodeService() {
            return Mockito.mock(BarcodeService.class);
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(barcodeService);
    }

    @Test
    void readBarcode_multipartFormData_success() throws Exception {
        MockMultipartFile barcodeFile = new MockMultipartFile(
                "data",
                "barcode.png",
                MediaType.IMAGE_PNG_VALUE,
                "some barcode image data".getBytes(StandardCharsets.UTF_8)
        );

        Result mockResult = new Result(
                "decodedText123",
                null,
                null,
                BarcodeFormat.QR_CODE,
                System.currentTimeMillis()
        );

        when(barcodeService.read(any(MultipartFile.class), any())).thenReturn(mockResult);

        mockMvc.perform(multipart("/read")
                                .file(barcodeFile)
                        // .param("hint[PURE_BARCODE]", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("decodedText123"))
                .andExpect(jsonPath("$.barcodeFormat").value("QR_CODE"));
    }

    @Test
    void readBarcode_multipartFormData_notFound() throws Exception {
        MockMultipartFile barcodeFile = new MockMultipartFile(
                "data",
                "barcode.png",
                MediaType.IMAGE_PNG_VALUE,
                "some barcode image data".getBytes(StandardCharsets.UTF_8)
        );

        when(barcodeService.read(any(MultipartFile.class), any())).thenThrow(NotFoundException.getNotFoundInstance());

        mockMvc.perform(multipart("/read")
                        .file(barcodeFile))
                .andExpect(status().isNotFound());
    }

    @Test
    void readBarcode_multipartFormData_ioException() throws Exception {
        MockMultipartFile barcodeFile = new MockMultipartFile(
                "data",
                "barcode.png",
                MediaType.IMAGE_PNG_VALUE,
                "some barcode image data".getBytes(StandardCharsets.UTF_8)
        );

        when(barcodeService.read(any(MultipartFile.class), any())).thenThrow(new IOException("File read error"));

        mockMvc.perform(multipart("/read")
                        .file(barcodeFile))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void readBarcode_applicationJson_success() throws Exception {
        Result mockResult = new Result(
                "jsonDecodedText",
                null,
                null,
                BarcodeFormat.CODE_128,
                System.currentTimeMillis()
        );

        when(barcodeService.read(anyString(), any())).thenReturn(mockResult);

        String jsonPayload = "{\"data\": \"base64encodedimagedata_as_string\"}";

        mockMvc.perform(post("/read")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload)
                        .param("hint[TRY_HARDER]", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("jsonDecodedText"))
                .andExpect(jsonPath("$.barcodeFormat").value("CODE_128"));
    }

    @Test
    void readBarcode_applicationJson_notFound() throws Exception {
        when(barcodeService.read(anyString(), any())).thenThrow(NotFoundException.getNotFoundInstance());

        String jsonPayload = "{\"data\": \"base64encoded-image-data-as-string\"}";

        mockMvc.perform(post("/read")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void readBarcode_applicationJson_ioException() throws Exception {
        when(barcodeService.read(anyString(), any())).thenThrow(new IOException("Data decoding error"));

        String jsonPayload = "{\"data\": \"base64encoded-image-data-as-string\"}";

        mockMvc.perform(post("/read")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isInternalServerError());
    }
}