package com.root14.barcodeservice.service;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.root14.barcodeservice.core.BarcodeReader;
import com.root14.barcodeservice.core.Generator;
import com.root14.barcodeservice.core.QrGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class BarcodeService {
    private final ApplicationContext applicationContext;

    //whole project have one reader, so no need to use spring ioc
    private final BarcodeReader barcodeReader = new BarcodeReader();
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Autowired
    public BarcodeService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Result read(MultipartFile data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        return barcodeReader.read(data.getInputStream(), hints);
    }

    public Result read(String data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        byte[] decoded = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(decoded);
        return barcodeReader.read(inputStream, hints);
    }

    //todo add return type -> return as png,jpg or base64
    public byte[] generate(String type, String data, int width, int height) throws WriterException, IOException {
        Generator generator = switch (type) {
            case "qr" -> applicationContext.getBean(QrGenerator.class);
            default -> throw new IllegalArgumentException("unknown type");
        };

        BufferedImage generated = generator.generate(data, width, height);
        //clears the previous stream
        byteArrayOutputStream.reset();
        ImageIO.write(generated, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
