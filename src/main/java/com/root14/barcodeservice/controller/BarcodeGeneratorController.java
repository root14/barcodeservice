package com.root14.barcodeservice.controller;

import com.google.zxing.WriterException;
import com.root14.barcodeservice.dto.ImageObject;
import com.root14.barcodeservice.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

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
     * Generates a barcode image based on the provided type and data, and returns the stored result.
     *
     * <p>This endpoint accepts the type of barcode (e.g., QR, CODE_128), the data to encode,
     * optional dimensions (width and height), and an optional name. It generates the barcode image,
     * stores it, and returns an {@link ImageObject} containing the UUID, image bytes, and creation timestamp.</p>
     *
     * <p>If barcode generation fails, a 500 Internal Server Error is returned.</p>
     *
     * @param type   the type of barcode to generate (e.g., "QR", "CODE_128")
     * @param data   the data to be encoded in the barcode
     * @param width  the width of the barcode image (optional, defaults to 400)
     * @param height the height of the barcode image (optional, defaults to 400)
     * @param store   an optional parameter for the generated barcode persist to database (default is false)
     * @return a ResponseEntity containing the generated {@link ImageObject}, or a 500 error if generation fails
     * @throws IOException     if an I/O error occurs during barcode generation
     * @throws WriterException if barcode encoding fails
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateBarcode(@RequestParam(value = "type", required = true) String type, @RequestParam(value = "data", required = true) String data, @RequestParam(value = "width", required = false, defaultValue = "400") int width, @RequestParam(value = "height", required = false, defaultValue = "400") int height, @RequestParam(value = "store", required = false, defaultValue = "false") boolean store) throws IOException, WriterException {
        Optional<ImageObject> storedImage = barcodeService.generate(type, data, width, height, store);

        if (storedImage.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }

        ImageObject image = storedImage.get();
        return ResponseEntity.ok().body(new ImageObject(image.uuid(), image.barcode(), image.createdAt()));
    }

    /**
     * Retrieves the previously stored barcode image associated with the given UUID.
     *
     * <p>This endpoint returns a PNG image as an HTTP response if a barcode with the specified
     * UUID exists. If no barcode is found, a 500 Internal Server Error is returned.</p>
     *
     * @param uuid the unique identifier used to locate the stored barcode
     * @return a ResponseEntity containing the barcode image as a PNG file if found,
     * or an Internal Server Error if not found
     */
    @GetMapping("/getBarcode")
    public ResponseEntity<?> getBarcode(@RequestParam(value = "uuid", required = true) String uuid) {
        Optional<ImageObject> storedImage = barcodeService.findBarcode(uuid);

        if (storedImage.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        ImageObject image = storedImage.get();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + image.uuid() + ".png").contentType(MediaType.IMAGE_PNG).body(image.barcode());
    }
}
