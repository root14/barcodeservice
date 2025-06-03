package com.root14.barcodeservice.controller;

import com.root14.barcodeservice.core.BarcodeResult;
import com.root14.barcodeservice.dto.ReadDto;
import com.root14.barcodeservice.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Map;

/**
 * REST controller responsible for handling barcode reading requests.
 * Supports reading barcodes from both multipart/form-data (e.g., image file)
 * and application/json payloads.
 */
@RestController
public class BarcodeReaderController {

    private final BarcodeService barcodeService;

    /**
     * Constructor for injecting the {@link BarcodeService}.
     *
     * @param barcodeService the service used to read barcodes
     */
    @Autowired
    public BarcodeReaderController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    /**
     * Reads a barcode from an uploaded image file.
     *
     * @param barcodeFile the uploaded file containing the barcode
     * @param hints optional decoding hints to help the barcode reader
     * @return a {@link ResponseEntity} containing the decoded barcode result
     * @throws NotFoundException if no barcode is found in the image
     * @throws IOException if an I/O error occurs while reading the file
     */
    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> readBarcode(
            @RequestParam(value = "data") MultipartFile barcodeFile,
            @RequestParam(value = "hint", required = false) Map<DecodeHintType, Object> hints)
            throws NotFoundException, IOException {

        Result result = barcodeService.read(barcodeFile, hints);
        return ResponseEntity.ok().body(new BarcodeResult(
                result.getTimestamp(),
                result.getText(),
                result.getBarcodeFormat()));
    }

    /**
     * Reads a barcode from base64-encoded or binary data provided in a JSON request.
     *
     * @param data the request body containing barcode data
     * @param hints optional decoding hints to help the barcode reader
     * @return a {@link ResponseEntity} containing the decoded barcode result
     * @throws NotFoundException if no barcode is found in the provided data
     * @throws IOException if an I/O error occurs during decoding
     */
    @PostMapping(value = "/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readBarcode(
            @RequestBody ReadDto data,
            @RequestParam(value = "hint", required = false) Map<DecodeHintType, Object> hints)
            throws NotFoundException, IOException {

        Result result = barcodeService.read(data.data(), hints);
        return ResponseEntity.ok().body(new BarcodeResult(
                result.getTimestamp(),
                result.getText(),
                result.getBarcodeFormat()));
    }
}
