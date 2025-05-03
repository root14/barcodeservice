package com.root14.barcodeservice.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.root14.barcodeservice.core.BarcodeGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class BarcodeController {


    @GetMapping("/generate")
    public ResponseEntity<?> generateQr(@RequestParam(value = "data", required = true) String data, @RequestParam(value = "minSize", required = false) String minSize, @RequestParam(value = "data", required = false) String width, @RequestParam(value = "data", required = false) String height) {

        if (minSize == null || (height == null && width == null)) {
            return ResponseEntity.badRequest().body("minSize or (height and width) must not be null.");
        }

        //todo dependency inject
        BarcodeGenerator qrGenerator = new BarcodeGenerator(new QRCodeWriter());

        try {
            BufferedImage bufferedImage = qrGenerator.generateBarcode(data, BarcodeFormat.QR_CODE, 400, 400);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + UUID.randomUUID()).contentType(MediaType.IMAGE_PNG).body(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }


    }

}
