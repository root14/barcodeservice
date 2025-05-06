package com.root14.barcodeservice.controller;

import com.google.zxing.WriterException;
import com.root14.barcodeservice.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
public class BarcodeGeneratorController {
    private final BarcodeService barcodeService;

    @Autowired
    public BarcodeGeneratorController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    /**
     * @param type   gonna created barcode type
     * @param data   barcode data -> todo add telephone, mailto: or etc
     * @param width  barcode width
     * @param height barcode height
     * @throws IOException
     * @throws WriterException
     */
    @GetMapping("/generate")
    public ResponseEntity<?> generateBarcode(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "data", required = true) String data,
            @RequestParam(value = "width", required = false) String width,
            @RequestParam(value = "height", required = false) String height) throws IOException, WriterException {

        if (height == null || width == null) {
            return ResponseEntity.badRequest().body("height and width must not be null.");
        }

        byte[] generated = barcodeService.generate(type, data, Integer.parseInt(width), Integer.parseInt(height));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + UUID.randomUUID() + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(generated);
    }
}
