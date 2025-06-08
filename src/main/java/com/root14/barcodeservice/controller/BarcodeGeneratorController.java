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
     * Generates a barcode image based on the provided type and data.
     *
     * <p>This endpoint accepts the barcode type (e.g., "QR", "CODE_128"), the data to encode,
     * optional dimensions (width and height), and a flag to store the generated barcode.
     * It returns an {@link ImageObject} containing the UUID, image bytes, and creation timestamp.</p>
     *
     * @param type   The type of barcode to generate (e.g., "QR", "CODE_128"). This parameter is **required**.
     * @param data   The data to be encoded in the barcode. This parameter is **required**.
     * @param width  The width of the barcode image in pixels (optional, defaults to 400).
     * @param height The height of the barcode image in pixels (optional, defaults to 400).
     * @param store  If {@code true}, the generated barcode will be persisted to the database (optional, defaults to {@code false}).
     * @return A {@link ResponseEntity} containing the generated {@link ImageObject} on success.
     * Returns a 404 Not Found if the barcode generation service returns an empty result,
     * or a 500 Internal Server Error if an unexpected error occurs during generation.
     * @throws IOException     If an I/O error occurs during barcode generation.
     * @throws WriterException If an error occurs during barcode encoding (e.g., invalid data for the specified type).
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateBarcode(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "data", required = true) String data,
            @RequestParam(value = "width", required = false, defaultValue = "400") int width,
            @RequestParam(value = "height", required = false, defaultValue = "400") int height,
            @RequestParam(value = "store", required = false, defaultValue = "false") boolean store
    ) throws IOException, WriterException {
        Optional<ImageObject> storedImage = barcodeService.generate(type, data, width, height, store);

        if (storedImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ImageObject image = storedImage.get();
        return ResponseEntity.ok().body(new ImageObject(image.uuid(), image.barcode(), image.createdAt()));
    }

    /**
     * Retrieves a previously stored barcode image by its UUID.
     *
     * <p>This endpoint fetches a barcode associated with the provided UUID. If found, it returns the barcode
     * image as a PNG file with a 200 OK status. The image is sent with an `inline` content disposition,
     * suggesting browsers display it directly. If no barcode is found for the given UUID, a 404 Not Found
     * status is returned.</p>
     *
     * @param uuid The unique identifier (UUID) of the barcode to retrieve. This parameter is **required**.
     * @return A {@link ResponseEntity} containing:
     * <ul>
     * <li>The barcode image as a PNG byte array with HTTP status 200 OK if found.</li>
     * <li>HTTP status 404 Not Found if no barcode is associated with the provided UUID.</li>
     * </ul>
     */
    @GetMapping("/getBarcode")
    public ResponseEntity<?> getBarcode(@RequestParam(value = "uuid", required = true) String uuid) {
        Optional<ImageObject> storedImage = barcodeService.findBarcode(uuid);

        if (storedImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ImageObject image = storedImage.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + image.uuid() + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(image.barcode());
    }
}
