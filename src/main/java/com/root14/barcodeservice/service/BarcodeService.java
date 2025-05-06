package com.root14.barcodeservice.service;

import com.google.zxing.*;
import com.root14.barcodeservice.core.BarcodeGenerator;
import com.root14.barcodeservice.core.BarcodeReader;
import com.root14.barcodeservice.core.BarcodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
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

    private final BarcodeGenerator barcodeGenerator;
    private final BarcodeReader barcodeReader;
    private final ByteArrayOutputStream byteArrayOutputStream;

    private BarcodeType barcodeType;

    @Autowired
    public BarcodeService(ApplicationContext applicationContext, BarcodeGenerator barcodeGenerator, BarcodeReader barcodeReader, ByteArrayOutputStream byteArrayOutputStream) {
        this.applicationContext = applicationContext;
        this.barcodeGenerator = barcodeGenerator;
        this.barcodeReader = barcodeReader;
        this.byteArrayOutputStream = byteArrayOutputStream;
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
        barcodeType = BarcodeType.fromKey(type);

        barcodeGenerator.setBarcodeFormat(barcodeType.getFormat());
        barcodeGenerator.setWriter(applicationContext.getBean(barcodeType.getWriterClass()));

        BufferedImage generated = barcodeGenerator.generate(data, width, height);
        //clears the previous stream
        byteArrayOutputStream.reset();
        ImageIO.write(generated, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
