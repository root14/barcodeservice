package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public class BarcodeGenerator {
    private final QRCodeWriter qrCodeWriter;

    public BarcodeGenerator(QRCodeWriter qrCodeWriter) {
        this.qrCodeWriter = qrCodeWriter;
    }

    public BufferedImage generateBarcode(String data, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, Object> hints) throws WriterException {
        return generateBarcode(data, barcodeFormat, width, height, null, hints);
    }

    public BufferedImage generateBarcode(String data, BarcodeFormat barcodeFormat, int width, int height, MatrixToImageConfig config) throws WriterException {
        return generateBarcode(data, barcodeFormat, width, height, config, null);
    }

    public BufferedImage generateBarcode(String data, BarcodeFormat barcodeFormat, int width, int height) throws IOException, WriterException {
        return generateBarcode(data, barcodeFormat, width, height, null, null);
    }

    //todo add auto scale calculation

    /**
     * @param minSize 1:1, 1:2, 2:3
     */
    public void generateBarcode(String data, BarcodeFormat barcodeFormat, int minSize) {
        //todo calculate width and height
    }

    /**
     * @apiNote hints.put(EncodeHintType.CHARACTER_SET, " UTF - 8 ");
     * @apiNote hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
     */
    public BufferedImage generateBarcode(String data, BarcodeFormat format, int width, int height, MatrixToImageConfig config, Map<EncodeHintType, Object> hints) throws WriterException {
        BitMatrix bitMatrix = qrCodeWriter.encode(data, format, width, height, hints);

        return (config != null) ? MatrixToImageWriter.toBufferedImage(bitMatrix, config) : MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
