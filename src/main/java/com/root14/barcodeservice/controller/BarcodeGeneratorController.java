package com.root14.barcodeservice.controller;

import com.google.zxing.WriterException;
import com.root14.barcodeservice.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

/**
 * REST controller for handling barcode generation requests.
 * <p>
 * This controller exposes an endpoint to generate barcodes in various formats using query parameters.
 * The generated barcode is returned as a PNG image.
 * </p>
 * <p>
 * Example request:
 * <pre>
 * GET /generate?type=CODE_128&data=12345678&width=300&height=100
 * </pre>
 * <p>
 * Required parameters:
 * <ul>
 *   <li><b>type</b> - The barcode format to generate (e.g., CODE_128, QR_CODE, etc.).</li>
 *   <li><b>data</b> - The content to encode within the barcode.</li>
 *   <li><b>width</b> - The desired width of the barcode image.</li>
 *   <li><b>height</b> - The desired height of the barcode image.</li>
 * </ul>
 * <p>
 * Response:
 * <ul>
 *   <li>HTTP 200 with image/png content if successful</li>
 *   <li>HTTP 400 if width or height is missing</li>
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link BarcodeService} for actual barcode image generation.</li>
 * </ul>
 */
@RestController
public class BarcodeGeneratorController {
    private final BarcodeService barcodeService;

    /**
     * Constructs the BarcodeGeneratorController with the given BarcodeService.
     *
     * @param barcodeService the service used to generate barcode images
     */
    @Autowired
    public BarcodeGeneratorController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    /**
     * Handles GET requests to generate a barcode image.
     *
     * @param type   the barcode format (e.g., CODE_128, QR_CODE)
     * @param data   the content to encode in the barcode
     * @param width  the desired width of the barcode image
     * @param height the desired height of the barcode image
     * @return a ResponseEntity containing the generated PNG image or an error message
     * @throws IOException     if an I/O error occurs during barcode generation
     * @throws WriterException if the barcode writer fails to encode the data
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateBarcode(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "data", required = true) String data, @RequestParam(value = "width", required = false) String width, @RequestParam(value = "height", required = false) String height) throws IOException, WriterException {

        if (height == null || width == null) {
            return ResponseEntity.badRequest().body("height and width must not be null.");
        }

        byte[] generated = barcodeService.generate(type, data, Integer.parseInt(width), Integer.parseInt(height));

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + UUID.randomUUID() + ".png").contentType(MediaType.IMAGE_PNG).body(generated);
    }
}
