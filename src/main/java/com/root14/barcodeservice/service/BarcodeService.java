package com.root14.barcodeservice.service;

import com.google.zxing.*;
import com.root14.barcodeservice.core.BarcodeGenerator;
import com.root14.barcodeservice.core.BarcodeReader;
import com.root14.barcodeservice.core.BarcodeType;
import com.root14.barcodeservice.entity.BarcodeEntity;
import com.root14.barcodeservice.repository.BarcodeRepository;
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
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class BarcodeService {
    private final ApplicationContext applicationContext;

    //removed from Spring ioc. they created once anyway.
    private final BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
    private final BarcodeReader barcodeReader = new BarcodeReader();
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private final BarcodeRepository barcodeRepository;

    @Autowired
    public BarcodeService(ApplicationContext applicationContext, BarcodeRepository barcodeRepository) {
        this.applicationContext = applicationContext;
        this.barcodeRepository = barcodeRepository;
    }

    public Result read(MultipartFile data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        return barcodeReader.read(data.getInputStream(), hints);
    }

    public Result read(String data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        byte[] decoded = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(decoded);
        return barcodeReader.read(inputStream, hints);
    }

    public byte[] generate(String type, String data, int width, int height, String desiredName) throws WriterException, IOException {
        BarcodeType barcodeType = BarcodeType.fromKey(type);

        barcodeGenerator.setBarcodeFormat(barcodeType.getFormat());
        barcodeGenerator.setWriter(applicationContext.getBean(barcodeType.getWriterClass()));

        BufferedImage generated = barcodeGenerator.generate(data, width, height);
        //clears the previous stream
        byteArrayOutputStream.reset();
        ImageIO.write(generated, "png", byteArrayOutputStream);

        //save
        if (!desiredName.isEmpty()) {
            BarcodeEntity barcodeEntity = new BarcodeEntity(desiredName, Instant.now());
            barcodeRepository.save(barcodeEntity);
        }

        return byteArrayOutputStream.toByteArray();
    }
}
