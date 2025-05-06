package com.root14.barcodeservice.controller;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.root14.barcodeservice.core.model.BarcodeResult;
import com.root14.barcodeservice.dto.ReadDto;
import com.root14.barcodeservice.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class BarcodeReaderController {
    private final BarcodeService barcodeService;

    @Autowired
    public BarcodeReaderController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }
    
    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> readBarcode(@RequestParam(value = "data") MultipartFile barcodeFile, @RequestParam(value = "hint", required = false) Map<DecodeHintType, Object> hints) throws NotFoundException, IOException {
        Result result = barcodeService.read(barcodeFile, hints);
        return ResponseEntity.ok().body(new BarcodeResult(
                        result.getTimestamp(),
                        result.getText(),
                        result.getBarcodeFormat()));
    }

    @PostMapping(value = "/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readBarcode(@RequestBody ReadDto data, @RequestParam(value = "hint", required = false) Map<DecodeHintType, Object> hints) throws NotFoundException, IOException {
        Result result = barcodeService.read(data.data(), hints);
        return ResponseEntity.ok().body(new BarcodeResult(
                result.getTimestamp(),
                result.getText(),
                result.getBarcodeFormat()));
    }
}
